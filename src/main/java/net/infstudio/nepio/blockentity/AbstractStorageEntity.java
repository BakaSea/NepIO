package net.infstudio.nepio.blockentity;

import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
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
        protected T getBlankVariant() {
            return AbstractStorageEntity.this.getBlankVariant();
        }

        @Override
        protected long getCapacity(T variant) {
            return AbstractStorageEntity.this.getCapacity(variant);
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

    protected long getCapacity(T variant) {
        return block.getCapacity(variant);
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
