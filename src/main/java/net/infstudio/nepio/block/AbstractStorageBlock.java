package net.infstudio.nepio.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

public abstract class AbstractStorageBlock extends BlockWithEntity {

    public static final IntProperty LEVEL = IntProperty.of("level", 1, 5);

    public AbstractStorageBlock(int level) {
        super(FabricBlockSettings.of(Material.METAL));
        setDefaultState(getStateManager().getDefaultState().with(LEVEL, level));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LEVEL);
    }

}
