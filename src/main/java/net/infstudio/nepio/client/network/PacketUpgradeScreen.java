package net.infstudio.nepio.client.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.infstudio.nepio.blockentity.NepCableEntity;
import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.logging.log4j.util.TriConsumer;

public class PacketUpgradeScreen implements IPacket, IExecutableServer {

    private BlockPos pos;
    private Direction direction;
    private int index;
    private int upgradeSize;
    private NbtCompound nbt;
    private PacketResult result;

    public PacketUpgradeScreen(PacketByteBuf buf) {
        fromPacket(buf);
    }

    public PacketUpgradeScreen(BlockPos pos, Direction direction, int index, int upgradeSize, NbtCompound nbt, PacketResult result) {
        this.pos = pos;
        this.direction = direction;
        this.index = index;
        this.upgradeSize = upgradeSize;
        this.nbt = nbt;
        this.result = result;
    }

    @Override
    public void fromPacket(PacketByteBuf buf) {
        pos = buf.readBlockPos();
        if (buf.readBoolean()) {
            direction = Direction.byId(buf.readInt());
        } else {
            direction = null;
        }
        index = buf.readInt();
        upgradeSize = buf.readInt();
        nbt = buf.readNbt();
        result = PacketResult.values()[buf.readInt()];
    }

    @Override
    public void toPacket(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        if (direction != null) {
            buf.writeBoolean(true);
            buf.writeInt(direction.getId());
        } else {
            buf.writeBoolean(false);
        }
        buf.writeInt(index);
        buf.writeInt(upgradeSize);
        buf.writeNbt(nbt);
        buf.writeInt(result.ordinal());
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getUpgradeSize() {
        return upgradeSize;
    }

    public void setResult(PacketResult result) {
        this.result = result;
    }

    public NbtCompound getNbt() {
        return nbt;
    }

    public void setNbt(NbtCompound nbt) {
        this.nbt = nbt;
    }

    public PacketUpgradeScreen copy() {
        PacketByteBuf buf = PacketByteBufs.create();
        toPacket(buf);
        return new PacketUpgradeScreen(buf);
    }

    @Override
    public void executeOnServer(ServerPlayerEntity player) {
        BlockEntity blockEntity = player.world.getBlockEntity(pos);
        if (blockEntity instanceof NepCableEntity nepCable) {
            if (nepCable.getPart(direction) instanceof IUpgradeEntity upgradeEntity) {
                result.action.accept(upgradeEntity, player, this);
            }
        }
    }

    public enum PacketResult {
        COMMON((entity, player, packet) -> {}),
        CHANGE((entity, player, packet) -> {
            int newIndex = packet.getNbt().getInt("index");
            if (newIndex == 0) {
                player.openHandledScreen((NamedScreenHandlerFactory) entity);
            } else {
                PacketUpgradeScreen newPacket = packet.copy();
                newPacket.setIndex(newIndex);
                newPacket.setResult(COMMON);
                NbtCompound entityNbt = new NbtCompound();
                entity.writeNbt(entityNbt);
                newPacket.setNbt(entityNbt);
                IUpgrade upgrade = entity.getUpgrade(newIndex-1);
                player.openHandledScreen(upgrade.createExtendedScreenHandlerFactory(newPacket, entity, newIndex));
            }
        }),
        SYNC((entity, player, packet) -> {
            entity.readNbt(packet.getNbt());
            entity.markDirty();
        });
        TriConsumer<IUpgradeEntity, PlayerEntity, PacketUpgradeScreen> action;
        PacketResult(TriConsumer<IUpgradeEntity, PlayerEntity, PacketUpgradeScreen> action) {
            this.action = action;
        }
    }

}
