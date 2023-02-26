package net.infstudio.nepio.item;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.infstudio.nepio.block.AbstractStorageBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;

public class BarrelItem extends AbstractStorageItem<ItemVariant> {

    public BarrelItem(Block block) {
        super(block);
    }

    @Override
    protected ItemVariant getBlankVariant() {
        return ItemVariant.blank();
    }

    @Override
    protected void readStorageNbt(SingleVariantStorage<ItemVariant> storage, NbtCompound nbt) {
        storage.variant = ItemVariant.fromNbt(nbt.getCompound("variant"));
        storage.amount = nbt.getLong("amount");
    }

    @Override
    protected MutableText getVariantName(ItemVariant variant) {
        return variant.getItem().getName().shallowCopy();
    }

    @Override
    protected String getAmountCapacity(long amount, long capacity) {
        return ((AbstractStorageBlock<ItemVariant>) getBlock()).isCreative() ? "∞" : amount+"/"+capacity;
    }

}
