package net.infstudio.nepio.item;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.infstudio.nepio.block.AbstractStorageBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;

public class TankItem extends AbstractStorageItem<FluidVariant>  {

    public TankItem(Block block) {
        super(block);
    }

    @Override
    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    @Override
    protected void readStorageNbt(SingleVariantStorage<FluidVariant> storage, NbtCompound nbt) {
        storage.variant = FluidVariant.fromNbt(nbt.getCompound("variant"));
        storage.amount = nbt.getLong("amount");
    }

    @Override
    protected MutableText getVariantName(FluidVariant variant) {
        return FluidVariantAttributes.getName(variant).shallowCopy();
    }

    @Override
    protected String getAmountCapacity(long amount, long capacity) {
        return ((AbstractStorageBlock<FluidVariant>)getBlock()).isCreative() ? "∞mB" : amount/81+"/"+capacity/81+"mB";
    }

    public static Storage<FluidVariant> getFluidStorage(ItemStack stack, ContainerItemContext context) {
        return ((TankItem) stack.getItem()).getStorage(stack, context);
    }

}
