package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.infstudio.nepio.NepIO;
import net.infstudio.nepio.screen.FilterUpgradeScreenHandler;
import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.infstudio.nepio.screen.PriorityUpgradeScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NIOScreenHandlers implements ModInitializer {

    public static final NIOScreenHandlers INSTANCE = new NIOScreenHandlers();

    public static final ExtendedScreenHandlerType<IOPortScreenHandler> IO_PORT_SCREEN_HANDLER = Registry.register(
        Registry.SCREEN_HANDLER,
        new Identifier(NepIO.MODID, "io_port_screen_handler"),
        new ExtendedScreenHandlerType<>(IOPortScreenHandler::new)
    );

    public static final ExtendedScreenHandlerType<FilterUpgradeScreenHandler> FILTER_UPGRADE_SCREEN_HANDLER = Registry.register(
        Registry.SCREEN_HANDLER,
        new Identifier(NepIO.MODID, "filter_upgrade_screen_handler"),
        new ExtendedScreenHandlerType<>(FilterUpgradeScreenHandler::new)
    );

    public static final ExtendedScreenHandlerType<PriorityUpgradeScreenHandler> PRIORITY_UPGRADE_SCREEN_HANDLER = Registry.register(
        Registry.SCREEN_HANDLER,
        new Identifier(NepIO.MODID, "priority_upgrade_screen_handler"),
        new ExtendedScreenHandlerType<>(PriorityUpgradeScreenHandler::new)
    );

    @Override
    public void onInitialize() {

    }

}
