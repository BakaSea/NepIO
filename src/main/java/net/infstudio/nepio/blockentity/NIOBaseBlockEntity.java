package net.infstudio.nepio.blockentity;

import net.infstudio.nepio.network.api.INetworkEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.infstudio.nepio.network.NNetworkNode;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Base block entity of Nep IO (Network block entity).
 * Use with {@link net.infstudio.nepio.block.NIOBaseBlock}
 */
public abstract class NIOBaseBlockEntity extends BlockEntity implements INetworkEntity {

    protected NNetworkNode networkNode;
    protected Item blockItem;

    public NIOBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Item blockItem) {
        super(type, pos, state);
        this.blockItem = blockItem;
    }

    @Override
    public void setNetworkNode(NNetworkNode networkNode) {
        this.networkNode = networkNode;
    }

    @Override
    public NNetworkNode getNetworkNode() {
        return networkNode;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public List<BlockPos> getPossibleConnection() {
        return Arrays.stream(Direction.values()).map(direction -> pos.offset(direction)).collect(Collectors.toList());
    }

    @Override
    public boolean canConnect(BlockPos pos) {
        return getPossibleConnection().contains(pos);
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    public ActionResult useWrench(ItemUsageContext context) {
        World world = context.getWorld();
        if (context.getPlayer().isSneaking()) {
            world.removeBlock(context.getBlockPos(), false);
            Vec3d hitPos = context.getHitPos();
            world.spawnEntity(new ItemEntity(world, hitPos.x, hitPos.y, hitPos.z, new ItemStack(blockItem)));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public void dropItems(World world, BlockPos pos) {

    }

}
