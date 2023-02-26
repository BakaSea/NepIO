package net.infstudio.nepio.blockentity;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.infstudio.nepio.block.AbstractStorageBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStorageEntity<T extends TransferVariant<?>> extends BlockEntity implements WrenchableEntity {

    protected final AbstractStorageBlock<T> block;

    protected final SingleVariantStorage<T> storage = new SingleVariantStorage<T>() {

        @Override
        public long insert(T insertedVariant, long maxAmount, TransactionContext transaction) {
            if (isCreative()) {
                StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);
                if (variant.isBlank() && canInsert(insertedVariant)) {
                    if (maxAmount > 0) {
                        updateSnapshots(transaction);
                        if (variant.isBlank()) {
                            variant = insertedVariant;
                            amount = 1;
                        }
                        return maxAmount;
                    }
                }
                return 0;
            } else return super.insert(insertedVariant, maxAmount, transaction);
        }

        @Override
        public long extract(T extractedVariant, long maxAmount, TransactionContext transaction) {
            if (isCreative()) {
                StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);
                if (extractedVariant.equals(variant) && canExtract(extractedVariant)) {
                    if (maxAmount > 0) {
                        updateSnapshots(transaction);
                        return maxAmount;
                    }
                }
                return 0;
            } else return super.extract(extractedVariant, maxAmount, transaction);
        }

        @Override
        protected T getBlankVariant() {
            return AbstractStorageEntity.this.getBlankVariant();
        }

        @Override
        protected long getCapacity(T variant) {
            return block.getCapacity(variant);
        }

        @Override
        protected void onFinalCommit() {
            super.onFinalCommit();
            markDirty();
            if (!world.isClient) {
                BlockState state = world.getBlockState(pos);
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
            }
        }

    };

    public AbstractStorageEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        block = (AbstractStorageBlock<T>) state.getBlock();
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

    public boolean isCreative() {
        return block.isCreative();
    }

    @Override
    public ActionResult useWrench(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        if (context.getPlayer().isSneaking()) {
            Vec3d hitPos = context.getHitPos();
            world.spawnEntity(new ItemEntity(world, hitPos.x, hitPos.y, hitPos.z, block.getPickStack(world, blockPos, getCachedState())));
            world.removeBlock(blockPos, false);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

}
