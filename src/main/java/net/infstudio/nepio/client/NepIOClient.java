package net.infstudio.nepio.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.infstudio.nepio.client.gui.screen.FilterUpgradeScreen;
import net.infstudio.nepio.client.gui.screen.IOPortScreen;
import net.infstudio.nepio.client.gui.screen.InputPortScreen;
import net.infstudio.nepio.client.gui.screen.PriorityUpgradeScreen;
import net.infstudio.nepio.client.render.NepCableBER;
import net.infstudio.nepio.registry.NIOBlockEntities;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class NepIOClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(NIOBlockEntities.NEP_CABLE_ENTITY, NepCableBER::new);

        HandledScreens.register(NIOScreenHandlers.IO_PORT_SCREEN_HANDLER, IOPortScreen<IOPortScreenHandler>::new);
        HandledScreens.register(NIOScreenHandlers.INPUT_PORT_SCREEN_HANDLER, InputPortScreen::new);
        HandledScreens.register(NIOScreenHandlers.FILTER_UPGRADE_SCREEN_HANDLER, FilterUpgradeScreen::new);
        HandledScreens.register(NIOScreenHandlers.PRIORITY_UPGRADE_SCREEN_HANDLER, PriorityUpgradeScreen::new);
    }

}
