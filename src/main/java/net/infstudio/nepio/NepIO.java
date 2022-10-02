package net.infstudio.nepio;

import net.infstudio.nepio.network.NetworkEventHandler;
import net.infstudio.nepio.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class NepIO implements ModInitializer {

    public static final String MODID = "nepio";

    public static final ItemGroup NEP_GROUP = FabricItemGroupBuilder.create(
        new Identifier(MODID, "group"))
        .icon(() -> new ItemStack(NIOItems.WRENCH.get()))
        .build();

    @Override
    public void onInitialize() {
        NIOItems.INSTANCE.onInitialize();
        NIOBlocks.INSTANCE.onInitialize();
        NIOBlockEntities.INSTANCE.onInitialize();
        NIOScreenHandlers.INSTANCE.onInitialize();
        NIONetworkHandlers.INSTANCE.onInitialize();

        NetworkEventHandler.INSTANCE.onInitialize();
    }

}
