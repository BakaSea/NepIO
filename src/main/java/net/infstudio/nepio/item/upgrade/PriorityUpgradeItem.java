package net.infstudio.nepio.item.upgrade;

import net.infstudio.nepio.item.BaseItem;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.infstudio.nepio.network.api.upgrade.PriorityUpgrade;
import net.infstudio.nepio.registry.NIOItems;

public class PriorityUpgradeItem extends BaseItem implements IUpgradeItem {

    public PriorityUpgradeItem() {
        super(NIOItems.getDefaultSettings());
    }

    @Override
    public IUpgrade createUpgradeComponent() {
        return new PriorityUpgrade(null);
    }

}
