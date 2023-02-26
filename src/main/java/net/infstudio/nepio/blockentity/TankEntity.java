package net.infstudio.nepio.blockentity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.infstudio.nepio.registry.NIOBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class TankEntity extends AbstractStorageEntity<FluidVariant> {

    public TankEntity(BlockPos pos, BlockState state) {
        super(NIOBlockEntities.TANK_ENTITY, pos, state);
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

}
