package net.infstudio.nepio.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public abstract class AbstractStorageBlock extends BlockWithEntity {

    protected int level;

    public AbstractStorageBlock(int level, Settings settings) {
        super(settings);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);
        world.getBlockEntity(pos).setStackNbt(stack);
        return stack;
    }

}
