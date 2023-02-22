package net.infstudio.nepio.network.api.upgrade;

import net.infstudio.nepio.network.api.automation.IInsertable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

public class DistributionUpgrade implements IUpgrade {

    private BlockEntity blockEntity;
    private IInsertable.Mode mode;

    public DistributionUpgrade(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.mode = IInsertable.Mode.RANDOM;
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

    }

    @Override
    public void clean() {

    }

    public IInsertable.Mode getMode() {
        return mode;
    }

    public void setMode(IInsertable.Mode mode) {
        this.mode = mode;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        IUpgrade.super.readNbt(nbt);
        mode = IInsertable.Mode.values()[nbt.getInt("mode")];
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        IUpgrade.super.writeNbt(nbt);
        nbt.putInt("mode", mode.ordinal());
    }

}
