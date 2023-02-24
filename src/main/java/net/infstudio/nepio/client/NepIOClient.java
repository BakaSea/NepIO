package net.infstudio.nepio.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.infstudio.nepio.client.gui.screen.*;
import net.infstudio.nepio.client.render.NepCableBER;
import net.infstudio.nepio.client.render.TankBER;
import net.infstudio.nepio.registry.NIOBlockEntities;
import net.infstudio.nepio.registry.NIOBlocks;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class NepIOClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
            NIOBlocks.TANK_1.get(), NIOBlocks.TANK_2.get(), NIOBlocks.TANK_3.get(), NIOBlocks.TANK_4.get(), NIOBlocks.TANK_5.get());

        BlockEntityRendererRegistry.register(NIOBlockEntities.NEP_CABLE_ENTITY, NepCableBER::new);
        BlockEntityRendererRegistry.register(NIOBlockEntities.TANK_ENTITY, TankBER::new);

        HandledScreens.register(NIOScreenHandlers.IO_PORT_SCREEN_HANDLER, IOPortScreen<IOPortScreenHandler>::new);
        HandledScreens.register(NIOScreenHandlers.INPUT_PORT_SCREEN_HANDLER, InputPortScreen::new);
        HandledScreens.register(NIOScreenHandlers.FILTER_UPGRADE_SCREEN_HANDLER, FilterUpgradeScreen::new);
        HandledScreens.register(NIOScreenHandlers.PRIORITY_UPGRADE_SCREEN_HANDLER, PriorityUpgradeScreen::new);
        HandledScreens.register(NIOScreenHandlers.REDSTONE_UPGRADE_SCREEN_HANDLER, RedstoneUpgradeScreen::new);
    }

}
