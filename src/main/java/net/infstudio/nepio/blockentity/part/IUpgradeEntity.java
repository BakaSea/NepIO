package net.infstudio.nepio.blockentity.part;

import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.minecraft.inventory.Inventory;

public interface IUpgradeEntity {

    Inventory getUpgrades();

    IUpgrade getUpgrade(int index);

}
