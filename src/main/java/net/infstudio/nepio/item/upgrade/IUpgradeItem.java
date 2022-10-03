package net.infstudio.nepio.item.upgrade;

import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.minecraft.block.entity.BlockEntity;

public interface IUpgradeItem {

    IUpgrade createUpgradeComponent(BlockEntity blockEntity);

}
