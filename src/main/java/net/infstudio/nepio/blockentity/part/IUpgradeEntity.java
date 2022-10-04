package net.infstudio.nepio.blockentity.part;

import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

/**
 * Use with {@link IUpgrade} and {@link net.infstudio.nepio.item.upgrade.IUpgradeItem}.
 */
public interface IUpgradeEntity {

    Item getItem();

    Inventory getUpgrades();

    IUpgrade getUpgrade(int index);

    void setUpgrade(int index, IUpgrade upgrade);

    void removeUpgrade(int index);

    void readNbt(NbtCompound nbt);

    void writeNbt(NbtCompound nbt);

    void markDirty();

}
