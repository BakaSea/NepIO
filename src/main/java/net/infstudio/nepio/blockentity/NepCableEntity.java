package net.infstudio.nepio.blockentity;

import com.mojang.logging.LogUtils;
import net.infstudio.nepio.blockentity.part.PartBaseEntity;
import net.infstudio.nepio.item.part.IPartItem;
import net.infstudio.nepio.network.service.ConnectService;
import net.infstudio.nepio.network.service.PathService;
import net.infstudio.nepio.registry.NIOBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.infstudio.nepio.block.NepCable;
import net.infstudio.nepio.registry.NIOBlockEntities;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class NepCableEntity extends NIOBaseBlockEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private Set<Direction> banConnect;

    private Map<Direction, PartBaseEntity> partMap;

    public NepCableEntity(BlockPos pos, BlockState state) {
        super(NIOBlockEntities.NEP_CABLE_ENTITY, pos, state, NIOBlocks.NEP_CABLE.getItem());
        banConnect = new HashSet<>();
        partMap = new HashMap<>();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        int[] banDirs = nbt.getIntArray("ban");
        banConnect.clear();
        getDirectionFromIds(banConnect, banDirs);
        readPartNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putIntArray("ban", getDirectionIds(banConnect));
        writePartNbt(nbt);
    }

    private int[] getDirectionIds(Collection<Direction> directions) {
        int[] result = new int[directions.size()];
        int i = 0;
        for (Direction direction : directions) {
            result[i++] = direction.getId();
        }
        return result;
    }

    private void getDirectionFromIds(Collection<Direction> directions, int[] ids) {
        for (int id : ids) {
            directions.add(Direction.byId(id));
        }
    }

    public void addBanConnect(Direction direction) {
        banConnect.add(direction);
    }

    public void removeBanConnect(Direction direction) {
        banConnect.remove(direction);
    }

    public boolean isBanConnect(Direction direction) {
        return banConnect.contains(direction);
    }

    @Override
    public List<BlockPos> getPossibleConnection() {
        return super.getPossibleConnection().stream().filter(connection -> {
            Direction direction = Direction.fromVector(connection.subtract(pos));
            return !isBanConnect(direction) && !existPart(direction);
        }).collect(Collectors.toList());
    }

    public void updateConnection() {
        BlockState state = getCachedState();
        for (Direction direction : Direction.values()) {
            state = state.with(NepCable.PROPERTY_MAP.get(direction), false);
            BlockPos neighborPos = pos.offset(direction);
            if (world.isPosLoaded(neighborPos.getX(), neighborPos.getZ()) && world.getBlockEntity(neighborPos) instanceof NIOBaseBlockEntity neighbor) {
                if (canConnect(neighborPos) && neighbor.canConnect(neighborPos.offset(direction.getOpposite()))) {
                    state = state.with(NepCable.PROPERTY_MAP.get(direction), true);
                }
            }
        }
        world.setBlockState(pos, state);
    }

    private void readPartNbt(NbtCompound nbt) {
        DefaultedList<ItemStack> partList = DefaultedList.ofSize(6, ItemStack.EMPTY);
        Inventories.readNbt(nbt.getCompound("part"), partList);
        partMap.clear();
        for (int i = 0; i < 6; ++i) {
            ItemStack stack = partList.get(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof IPartItem part) {
                    Direction direction = Direction.byId(i);
                    PartBaseEntity partEntity = part.createPartEntity(this);
                    partMap.put(direction, partEntity);
                    partEntity.readNbt(nbt.getCompound(direction.getName()));
                }
            }
        }
    }

    private void writePartNbt(NbtCompound nbt) {
        DefaultedList<ItemStack> partList = DefaultedList.ofSize(6, ItemStack.EMPTY);
        for (var entry : partMap.entrySet()) {
            Direction direction = entry.getKey();
            PartBaseEntity partEntity = entry.getValue();
            if (partEntity != null) {
                partList.set(direction.getId(), new ItemStack(partEntity.getItem()));
                NbtCompound partNbt = new NbtCompound();
                partEntity.writeNbt(partNbt);
                nbt.put(direction.getName(), partNbt);
            }
        }
        NbtCompound invNbt = new NbtCompound();
        Inventories.writeNbt(invNbt, partList);
        nbt.put("part", invNbt);
    }

    public Map<Direction, PartBaseEntity> getPartMap() {
        return partMap;
    }

    public void addPart(Direction direction, PartBaseEntity partEntity) {
        LOGGER.info("Add part: "+partEntity.getItem().getName());
        partMap.put(direction, partEntity);
        PathService.INSTANCE.updateNetwork(networkNode);
        ConnectService.INSTANCE.updateConnection(this);
        markDirty();
    }

    public void removePart(Direction direction) {
        partMap.remove(direction);
        PathService.INSTANCE.updateNetwork(networkNode);
        ConnectService.INSTANCE.updateConnection(this);
        markDirty();
    }

    public boolean existPart(Direction direction) {
        return partMap.containsKey(direction);
    }

    @Override
    public void useWrench(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        Vec3d hitPos = context.getHitPos();
        Vec3d point = hitPos.subtract(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        if (context.getPlayer().isSneaking()) {
            for (var entry : partMap.entrySet()) {
                Direction direction = entry.getKey();
                PartBaseEntity part = entry.getValue();
                VoxelShape partShape = part.getItem().getShape(direction);
                if (touchBox(partShape.getBoundingBox(), point)) {
                    world.spawnEntity(new ItemEntity(world, hitPos.x, hitPos.y, hitPos.z, new ItemStack(part.getItem())));
                    removePart(direction);
                    BlockState state = world.getBlockState(pos);
                    world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
                    return;
                }
            }
            world.removeBlock(blockPos, false);
            world.spawnEntity(new ItemEntity(world, hitPos.x, hitPos.y, hitPos.z, new ItemStack(blockItem)));
            for (PartBaseEntity part : partMap.values()) {
                world.spawnEntity(new ItemEntity(world, hitPos.x, hitPos.y, hitPos.z, new ItemStack(part.getItem())));
            }
        } else {
            BlockState state = world.getBlockState(blockPos);
            VoxelShape shape = NepCable.getStateShape(state);
            if (touchBox(shape.getBoundingBox(), point)) {
                point = point.subtract(0.5D, 0.5D, 0.5D);
                Direction direction = Direction.getFacing(point.x, point.y, point.z);
                if (isBanConnect(direction)) {
                    removeBanConnect(direction);
                    PathService.INSTANCE.updateNetwork(networkNode);
                    ConnectService.INSTANCE.updateConnection(this);
                    markDirty();
                } else {
                    if (state.get(NepCable.PROPERTY_MAP.get(direction))) {
                        addBanConnect(direction);
                        PathService.INSTANCE.updateNetwork(networkNode);
                        ConnectService.INSTANCE.updateConnection(this);
                        markDirty();
                    }
                }
            }
        }
    }

    private static boolean touchBox(Box box, Vec3d point) {
        return box.minX <= point.x && point.x <= box.maxX && box.minY <= point.y && point.y <= box.maxY && box.minZ <= point.z && point.z <= box.maxZ;
    }

}
