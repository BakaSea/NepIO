package net.infstudio.nepio.blockentity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.infstudio.nepio.registry.NIOBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class BarrelEntity extends AbstractStorageEntity<ItemVariant> {

    public BarrelEntity(BlockPos pos, BlockState state) {
        super(NIOBlockEntities.BARREL_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        storage.variant = ItemVariant.fromNbt(nbt.getCompound("variant"));
    }

    @Override
    protected ItemVariant getBlankVariant() {
        return ItemVariant.blank();
    }

}
