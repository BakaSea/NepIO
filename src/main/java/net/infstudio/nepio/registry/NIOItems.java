package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.infstudio.nepio.NepIO;
import net.infstudio.nepio.block.TankBlock;
import net.infstudio.nepio.item.TankItem;
import net.infstudio.nepio.item.Wrench;
import net.infstudio.nepio.item.part.InputPort;
import net.infstudio.nepio.item.part.OutputPort;
import net.infstudio.nepio.item.upgrade.FilterUpgradeItem;
import net.infstudio.nepio.item.upgrade.PriorityUpgradeItem;
import net.infstudio.nepio.item.upgrade.RedstoneUpgradeItem;
import net.infstudio.nepio.item.upgrade.SpeedUpgradeItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class NIOItems implements ModInitializer {

    public static final NIOItems INSTANCE = new NIOItems();

    private static ArrayList<ItemDef> itemDefs = new ArrayList<>();

    public static final ItemDef WRENCH = new ItemDef(new Wrench(), "wrench");
    public static final ItemDef INPUT_PORT = new ItemDef(new InputPort(), "input_port");
    public static final ItemDef OUTPUT_PORT = new ItemDef(new OutputPort(), "output_port");

    public static final ItemDef FILTER_UPGRADE_BASIC = new ItemDef(new FilterUpgradeItem(1), "filter_upgrade_basic");
    public static final ItemDef FILTER_UPGRADE_ADVANCED = new ItemDef(new FilterUpgradeItem(3), "filter_upgrade_advanced");
    public static final ItemDef FILTER_UPGRADE_ULTIMATE = new ItemDef(new FilterUpgradeItem(9), "filter_upgrade_ultimate");
    public static final ItemDef PRIORITY_UPGRADE = new ItemDef(new PriorityUpgradeItem(), "priority_upgrade");
    public static final ItemDef SPEED_UPGRADE_1 = new ItemDef(new SpeedUpgradeItem(1), "speed_upgrade_1");
    public static final ItemDef SPEED_UPGRADE_2 = new ItemDef(new SpeedUpgradeItem(2), "speed_upgrade_2");
    public static final ItemDef SPEED_UPGRADE_3 = new ItemDef(new SpeedUpgradeItem(3), "speed_upgrade_3");
    public static final ItemDef SPEED_UPGRADE_4 = new ItemDef(new SpeedUpgradeItem(3), "speed_upgrade_4");
    public static final ItemDef SPEED_UPGRADE_5 = new ItemDef(new SpeedUpgradeItem(3), "speed_upgrade_5");
    public static final ItemDef REDSTONE_UPGRADE = new ItemDef(new RedstoneUpgradeItem(), "redstone_upgrade");

    public static final ItemDef TANK_1 = new ItemDef(new TankItem((TankBlock) NIOBlocks.TANK_1.get()), "tank_1");
    public static final ItemDef TANK_2 = new ItemDef(new TankItem((TankBlock) NIOBlocks.TANK_2.get()), "tank_2");
    public static final ItemDef TANK_3 = new ItemDef(new TankItem((TankBlock) NIOBlocks.TANK_3.get()), "tank_3");
    public static final ItemDef TANK_4 = new ItemDef(new TankItem((TankBlock) NIOBlocks.TANK_4.get()), "tank_4");
    public static final ItemDef TANK_5 = new ItemDef(new TankItem((TankBlock) NIOBlocks.TANK_5.get()), "tank_5");

    public void onInitialize() {
        for (ItemDef itemDef : itemDefs) {
            Registry.register(Registry.ITEM, new Identifier(NepIO.MODID, itemDef.getId()), itemDef.get());
        }
        FluidStorage.ITEM.registerForItems(TankItem::getFluidStorage, TANK_1.get(), TANK_2.get(), TANK_3.get(), TANK_4.get(), TANK_5.get());
    }

    public static class ItemDef extends RegistryDef<Item> {

        public ItemDef(Item definition, String id) {
            super(definition, id);
            itemDefs.add(this);
        }

    }

}
