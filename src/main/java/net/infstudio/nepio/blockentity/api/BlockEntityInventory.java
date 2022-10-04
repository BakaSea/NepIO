package net.infstudio.nepio.blockentity.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class BlockEntityInventory implements ImplementedInventory {

    private DefaultedList<ItemStack> items;
    private BlockEntity blockEntity;

    public BlockEntityInventory(int size, BlockEntity blockEntity) {
        this.items = DefaultedList.ofSize(size, ItemStack.EMPTY);
        this.blockEntity = blockEntity;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void markDirty() {
        blockEntity.markDirty();
    }

    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, items);
    }

    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
    }

}
