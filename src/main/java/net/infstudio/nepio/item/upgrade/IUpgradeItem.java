package net.infstudio.nepio.item.upgrade;

import net.infstudio.nepio.network.api.upgrade.IUpgrade;

/**
 * Use with {@link IUpgrade} and {@link net.infstudio.nepio.blockentity.part.IUpgradeEntity}.
 */
public interface IUpgradeItem {

    /**
     * Create a new empty upgrade component.
     */
    IUpgrade createUpgradeComponent();

}
