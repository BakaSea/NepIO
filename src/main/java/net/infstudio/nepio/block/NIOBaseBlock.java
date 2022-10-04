package net.infstudio.nepio.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;

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

}
