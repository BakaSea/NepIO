package net.infstudio.nepio.client.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.function.Supplier;

public abstract class PacketUpgradeScreen implements IPacket {

    private BlockPos pos;
    private Direction direction;
    private int index;
    private int upgradeSize;
    private static final Supplier<PacketUpgradeScreen>[] types = new Supplier[]{
        new PacketUpgrade(), new PacketFilter()
    };

    public PacketUpgradeScreen() {
        pos = null;
        direction = null;
        index = 0;
        upgradeSize = 0;
    }

    public PacketUpgradeScreen(PacketByteBuf buf) {
        fromPacket(buf);
    }

    public PacketUpgradeScreen(BlockPos pos, Direction direction, int index, int upgradeSize) {
        this.pos = pos;
        this.direction = direction;
        this.index = index;
        this.upgradeSize = upgradeSize;
    }

    public static PacketUpgradeScreen of(PacketByteBuf buf) {
        PacketUpgradeScreen result = types[buf.readInt()].get();
        result.fromPacket(buf);
        return result;
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

    public int getUpgradeSize() {
        return upgradeSize;
    }

    public static class PacketUpgrade extends PacketUpgradeScreen implements Supplier<PacketUpgrade> {

        public PacketUpgrade() {

        }

        public PacketUpgrade(PacketByteBuf buf) {
            fromPacket(buf);
        }

        public PacketUpgrade(BlockPos pos, Direction direction, int index, int upgradeSize) {
            super(pos, direction, index, upgradeSize);
        }

        @Override
        public void fromPacket(PacketByteBuf buf) {
            super.fromPacket(buf);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeInt(0);
            super.toPacket(buf);
        }

        @Override
        public void executeOnClient() {

        }

        @Override
        public void executeOnServer() {

        }

        @Override
        public PacketUpgrade get() {
            return new PacketUpgrade();
        }

    }

    public static class PacketFilter extends PacketUpgradeScreen implements Supplier<PacketFilter> {

        public PacketFilter() {

        }

        public PacketFilter(PacketByteBuf buf) {
            fromPacket(buf);
        }

        @Override
        public void fromPacket(PacketByteBuf buf) {
            super.fromPacket(buf);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeInt(1);
            super.toPacket(buf);
        }

        @Override
        public void executeOnClient() {

        }

        @Override
        public void executeOnServer() {

        }

        @Override
        public PacketFilter get() {
            return new PacketFilter();
        }

    }

}
