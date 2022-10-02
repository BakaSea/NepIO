package net.infstudio.nepio.blockentity.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;

public class BlockEntityInventory extends SimpleInventory {

    private BlockEntity blockEntity;

    public BlockEntityInventory(int size, BlockEntity blockEntity) {
        super(size);
        this.blockEntity = blockEntity;
    }

    @Override
    public void markDirty() {
        blockEntity.markDirty();
    }

}
