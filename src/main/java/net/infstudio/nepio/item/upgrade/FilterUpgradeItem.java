package net.infstudio.nepio.item.upgrade;

import net.infstudio.nepio.item.BaseItem;
import net.infstudio.nepio.network.api.upgrade.FilterUpgrade;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.infstudio.nepio.registry.NIOItems;

public class FilterUpgradeItem extends BaseItem implements IUpgradeItem {

    private int size;

    public FilterUpgradeItem(int size) {
        super(NIOItems.getDefaultSettings());
        this.size = size;
    }

    @Override
    public IUpgrade createUpgradeComponent() {
        return new FilterUpgrade(size, null);
    }

}
