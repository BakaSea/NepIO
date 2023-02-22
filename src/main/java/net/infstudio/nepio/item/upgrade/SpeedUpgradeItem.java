package net.infstudio.nepio.item.upgrade;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.infstudio.nepio.item.BaseItem;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.infstudio.nepio.network.api.upgrade.SpeedUpgrade;

public class SpeedUpgradeItem extends BaseItem implements IUpgradeItem {

    private int level;

    public SpeedUpgradeItem(int level) {
        super(new FabricItemSettings());
        this.level = level;
    }

    @Override
    public IUpgrade createUpgradeComponent() {
        return new SpeedUpgrade(level, null);
    }

}
