package net.infstudio.nepio.item;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.infstudio.nepio.block.AbstractStorageBlock;
import net.infstudio.nepio.registry.NIOItems;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractStorageItem<T extends TransferVariant<?>> extends BlockItem {

    public AbstractStorageItem(Block block) {
        super(block, NIOItems.getDefaultSettings());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        SingleVariantStorage<T> storage = getStorage(stack, null);
        if (!storage.isResourceBlank()) {
            tooltip.add(getVariantName(storage.variant).append(new LiteralText(": "+getAmountCapacity(storage.amount, getCapacity(storage.variant)))));
        }
    }

    protected abstract T getBlankVariant();

    protected long getCapacity(T variant) {
        return ((AbstractStorageBlock<T>) getBlock()).getCapacity(variant);
    }

    protected abstract void readStorageNbt(SingleVariantStorage<T> storage, NbtCompound nbt);

    protected abstract MutableText getVariantName(T variant);

    protected abstract String getAmountCapacity(long amount, long capacity);

    protected ItemStorage getStorage(ItemStack stack, ContainerItemContext context) {
        return new ItemStorage(stack, context);
    }

    public class ItemStorage extends SingleVariantStorage<T> {

        private ItemStack stack;
        private ContainerItemContext context;

        public ItemStorage(ItemStack stack, ContainerItemContext context) {
            this.stack = stack;
            this.context = context;
            readStorageNbt(this, stack.getOrCreateSubNbt("BlockEntityTag"));
        }

        @Override
        protected T getBlankVariant() {
            return AbstractStorageItem.this.getBlankVariant();
        }

        @Override
        protected long getCapacity(T variant) {
            return AbstractStorageItem.this.getCapacity(variant);
        }

        @Override
        public long insert(T insertedVariant, long maxAmount, TransactionContext transaction) {
            long k = 0;
            if (((AbstractStorageBlock<T>) getBlock()).isCreative()) {
                StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);
                if (variant.isBlank() && canInsert(insertedVariant)) {
                    if (maxAmount > 0) {
                        updateSnapshots(transaction);
                        if (variant.isBlank()) {
                            variant = insertedVariant;
                            amount = 1;
                        }
                        k = maxAmount;
                    }
                }
            } else k = super.insert(insertedVariant, maxAmount, transaction);
            updateItemStack(transaction);
            return k;
        }

        @Override
        public long extract(T extractedVariant, long maxAmount, TransactionContext transaction) {
            long k = 0;
            if (((AbstractStorageBlock<T>) getBlock()).isCreative()) {
                StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);
                if (extractedVariant.equals(variant) && canExtract(extractedVariant)) {
                    if (maxAmount > 0) {
                        updateSnapshots(transaction);
                        k = maxAmount;
                    }
                }
            } else k = super.extract(extractedVariant, maxAmount, transaction);
            updateItemStack(transaction);
            return k;
        }

        private void updateItemStack(TransactionContext transaction) {
            ItemStack newStack = stack.copy();
            NbtCompound nbt = newStack.getOrCreateSubNbt("BlockEntityTag");
            nbt.put("variant", variant.toNbt());
            nbt.putLong("amount", amount);
            try (Transaction t = transaction.openNested()) {
                context.exchange(ItemVariant.of(newStack), 1, t);
                t.commit();
            }
        }

    }

}
