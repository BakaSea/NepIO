package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.infstudio.nepio.block.BarrelBlock;
import net.infstudio.nepio.block.TankBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.infstudio.nepio.NepIO;
import net.infstudio.nepio.block.NepCable;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class NIOBlocks implements ModInitializer {

    public static final NIOBlocks INSTANCE = new NIOBlocks();

    private static ArrayList<BlockDef> blockDefs = new ArrayList<>();

    public static final BlockDef NEP_CABLE = new BlockDef(new NepCable(), "nep_cable");

    public static final BlockDef BARREL_1 = new BlockDef(new BarrelBlock(1), "barrel_1", true);
    public static final BlockDef BARREL_2 = new BlockDef(new BarrelBlock(2), "barrel_2", true);
    public static final BlockDef BARREL_3 = new BlockDef(new BarrelBlock(3), "barrel_3", true);
    public static final BlockDef BARREL_4 = new BlockDef(new BarrelBlock(4), "barrel_4", true);
    public static final BlockDef BARREL_5 = new BlockDef(new BarrelBlock(5), "barrel_5", true);


    public static final BlockDef TANK_1 = new BlockDef(new TankBlock(1), "tank_1", true);
    public static final BlockDef TANK_2 = new BlockDef(new TankBlock(2), "tank_2", true);
    public static final BlockDef TANK_3 = new BlockDef(new TankBlock(3), "tank_3", true);
    public static final BlockDef TANK_4 = new BlockDef(new TankBlock(4), "tank_4", true);
    public static final BlockDef TANK_5 = new BlockDef(new TankBlock(5), "tank_5", true);

    public void onInitialize() {
        for (BlockDef blockDef : blockDefs) {
            Registry.register(Registry.BLOCK, new Identifier(NepIO.MODID, blockDef.getId()), blockDef.get());
            if (!blockDef.isIndependentItem()) Registry.register(Registry.ITEM, new Identifier(NepIO.MODID, blockDef.getId()), blockDef.getItem());
        }
    }

    public static FabricBlockSettings getDefaultSettings() {
        return FabricBlockSettings.of(Material.METAL).strength(2.0F);
    }

    public static class BlockDef extends RegistryDef<Block> {

        private BlockItem item;
        private boolean independentItem;

        public BlockDef(Block definition, String id) {
            super(definition, id);
            item = new BlockItem(definition, NIOItems.getDefaultSettings());
            blockDefs.add(this);
        }

        public BlockDef(Block definition, String id, boolean independentItem) {
            super(definition, id);
            this.independentItem = independentItem;
            if (!independentItem) item = new BlockItem(definition, new FabricItemSettings().group(NepIO.NEP_GROUP));
            blockDefs.add(this);
        }

        public boolean isIndependentItem() {
            return independentItem;
        }

        public BlockItem getItem() {
            return item;
        }

    }

}
