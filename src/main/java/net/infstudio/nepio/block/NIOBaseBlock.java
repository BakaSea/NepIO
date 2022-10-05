package net.infstudio.nepio.block;

import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base block of Nep IO (Network block).
 * Use with {@link net.infstudio.nepio.blockentity.NIOBaseBlockEntity}.
 */
public abstract class NIOBaseBlock extends BlockWithEntity {

    public NIOBaseBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof NIOBaseBlockEntity blockEntity) {
                blockEntity.dropItems(world, pos);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

}
