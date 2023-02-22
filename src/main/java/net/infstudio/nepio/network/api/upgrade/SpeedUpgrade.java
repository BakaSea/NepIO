package net.infstudio.nepio.network.api.upgrade;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

public class SpeedUpgrade implements IUpgrade {

    private int level;
    private BlockEntity blockEntity;

    public SpeedUpgrade(int level, BlockEntity blockEntity) {
        this.level = level;
        this.blockEntity = blockEntity;
    }

    public int getSpeed() {
        return 1 << (level << 1);
    }

    @Override
    public void markDirty() {
        blockEntity.markDirty();
    }

    @Override
    public boolean hasScreen() {
        return false;
    }

    @Override
    public void copyFrom(IUpgrade other) {
        if (other instanceof SpeedUpgrade o) {
            level = o.level;
        }
    }

    @Override
    public void clean() {
        level = 0;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        IUpgrade.super.readNbt(nbt);
        level = nbt.getInt("level");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        IUpgrade.super.writeNbt(nbt);
        nbt.putInt("level", level);
    }

}
