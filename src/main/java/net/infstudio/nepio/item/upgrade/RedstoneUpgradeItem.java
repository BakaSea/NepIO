package net.infstudio.nepio.item.upgrade;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.infstudio.nepio.item.BaseItem;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.infstudio.nepio.network.api.upgrade.RedstoneUpgrade;

public class RedstoneUpgradeItem extends BaseItem implements IUpgradeItem {

    public RedstoneUpgradeItem() {
        super(new FabricItemSettings());
    }

    @Override
    public IUpgrade createUpgradeComponent() {
        return new RedstoneUpgrade(null);
    }

}
