package net.infstudio.nepio.blockentity.part;

import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

public interface IUpgradeEntity {

    Item getItem();

    Inventory getUpgrades();

    IUpgrade getUpgrade(int index);

    void readNbt(NbtCompound nbt);

    void writeNbt(NbtCompound nbt);

}
