package net.infstudio.nepio.block;

import net.minecraft.block.BlockWithEntity;

public abstract class AbstractStorageBlock extends BlockWithEntity {

    protected int level;

    public AbstractStorageBlock(int level, Settings settings) {
        super(settings);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
