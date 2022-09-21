package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
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

    public void onInitialize() {
        for (BlockDef blockDef : blockDefs) {
            Registry.register(Registry.BLOCK, new Identifier(NepIO.MODID, blockDef.getId()), blockDef.get());
            Registry.register(Registry.ITEM, new Identifier(NepIO.MODID, blockDef.getId()), blockDef.getItem());
        }
    }

    public static class BlockDef extends RegistryDef<Block> {

        private BlockItem item;

        public BlockDef(Block definition, String id) {
            super(definition, id);
            item = new BlockItem(definition, new FabricItemSettings().group(NepIO.NEP_GROUP));
            blockDefs.add(this);
        }

        public BlockItem getItem() {
            return item;
        }

    }

}
