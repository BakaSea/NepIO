package net.infstudio.nepio.item.upgrade;

import net.infstudio.nepio.item.BaseItem;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.infstudio.nepio.network.api.upgrade.RedstoneUpgrade;
import net.infstudio.nepio.registry.NIOItems;

public class RedstoneUpgradeItem extends BaseItem implements IUpgradeItem {

    public RedstoneUpgradeItem() {
        super(NIOItems.getDefaultSettings());
    }

    @Override
    public IUpgrade createUpgradeComponent() {
        return new RedstoneUpgrade(null);
    }

}
