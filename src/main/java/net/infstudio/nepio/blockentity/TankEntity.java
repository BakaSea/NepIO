package net.infstudio.nepio.blockentity;

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.infstudio.nepio.block.AbstractStorageBlock;
import net.infstudio.nepio.block.TankBlock;
import net.infstudio.nepio.registry.NIOBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class TankEntity extends AbstractStorageEntity<FluidVariant> {

    public TankEntity(BlockPos pos, BlockState state) {
        super(NIOBlockEntities.TANK_ENTITY, pos, state);
        capacity = TankBlock.getCapacity(state.get(AbstractStorageBlock.LEVEL));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        storage.variant = FluidVariant.fromNbt(nbt.getCompound("variant"));
    }

    @Override
    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    @Override
    protected ItemApiLookup<Storage<FluidVariant>, ContainerItemContext> getLookup() {
        return FluidStorage.ITEM;
    }

}
