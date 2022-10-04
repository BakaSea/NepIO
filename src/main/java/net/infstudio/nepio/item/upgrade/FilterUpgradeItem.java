package net.infstudio.nepio.item.upgrade;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.infstudio.nepio.item.BaseItem;
import net.infstudio.nepio.network.api.upgrade.FilterUpgrade;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;

public class FilterUpgradeItem extends BaseItem implements IUpgradeItem {

    int size;

    public FilterUpgradeItem(int size) {
        super(new FabricItemSettings());
        this.size = size;
    }

    @Override
    public IUpgrade createUpgradeComponent() {
        return new FilterUpgrade(size, null);
    }

}
