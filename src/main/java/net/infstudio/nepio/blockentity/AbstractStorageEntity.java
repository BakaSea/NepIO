package net.infstudio.nepio.blockentity;

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStorageEntity<T extends TransferVariant<?>> extends BlockEntity {

    protected long capacity;

    protected final SingleVariantStorage<T> storage = new SingleVariantStorage<T>() {

        @Override
        protected T getBlankVariant() {
            return AbstractStorageEntity.this.getBlankVariant();
        }

        @Override
        protected long getCapacity(T variant) {
            return capacity;
        }

        @Override
        protected void onFinalCommit() {
            super.onFinalCommit();
            markDirty();
        }

    };

    public AbstractStorageEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        storage.variant = getBlankVariant();
    }

    public SingleVariantStorage<T> getStorage() {
        return storage;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        storage.amount = nbt.getLong("amount");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("variant", storage.variant.toNbt());
        nbt.putLong("amount", storage.amount);
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



    protected abstract T getBlankVariant();

    protected abstract ItemApiLookup<Storage<T>, ContainerItemContext> getLookup();

    public boolean insert(PlayerEntity player, Hand hand) {
        ItemStack itemStack = null;
        if (player.isCreative()) {
            itemStack = player.getStackInHand(hand).copy();
        }
        Storage<T> playerHand = ContainerItemContext.ofPlayerHand(player, hand).find(getLookup());
        boolean flag = StorageUtil.move(playerHand, storage, f -> true, Long.MAX_VALUE, null) > 0;
        if (player.isCreative() && itemStack != null) {
            player.setStackInHand(hand, itemStack);
        }
        return flag;
    }

    public boolean extract(PlayerEntity player, Hand hand) {
        ItemStack itemStack = null;
        if (player.isCreative()) {
            itemStack = player.getStackInHand(hand).copy();
        }
        Storage<T> playerHand = ContainerItemContext.ofPlayerHand(player, hand).find(getLookup());
        boolean flag = StorageUtil.move(storage, playerHand, f -> true, Long.MAX_VALUE, null) > 0;
        if (player.isCreative() && itemStack != null) {
            player.setStackInHand(hand, itemStack);
        }
        return flag;
    }

}
