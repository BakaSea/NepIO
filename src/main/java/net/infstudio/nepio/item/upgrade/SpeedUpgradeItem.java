package net.infstudio.nepio.item.upgrade;

import net.infstudio.nepio.item.BaseItem;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.infstudio.nepio.network.api.upgrade.SpeedUpgrade;
import net.infstudio.nepio.registry.NIOItems;

public class SpeedUpgradeItem extends BaseItem implements IUpgradeItem {

    private int level;

    public SpeedUpgradeItem(int level) {
        super(NIOItems.getDefaultSettings());
        this.level = level;
    }

    @Override
    public IUpgrade createUpgradeComponent() {
        return new SpeedUpgrade(level, null);
    }

}
