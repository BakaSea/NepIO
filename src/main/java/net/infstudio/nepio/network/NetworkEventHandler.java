package net.infstudio.nepio.network;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.infstudio.nepio.network.service.automation.TransferService;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.infstudio.nepio.network.service.ConnectService;
import net.infstudio.nepio.network.service.NetworkService;
import net.infstudio.nepio.network.service.PathService;

public class NetworkEventHandler implements ModInitializer {

    public static final NetworkEventHandler INSTANCE = new NetworkEventHandler();

    @Override
    public void onInitialize() {
        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register(this::onBlockEntityLoad);
        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register(this::onBlockEntityUnload);
        ServerTickEvents.END_SERVER_TICK.register(this::onServerEndTick);
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
    }

    private void onServerStarting(MinecraftServer server) {
        NetworkService.INSTANCE.onServerStarting();
        PathService.INSTANCE.onServerStarting();
        ConnectService.INSTANCE.onServerStarting();
    }

    private void onServerStopped(MinecraftServer server) {
        NetworkService.INSTANCE.onServerStopped();
    }

    private void onBlockEntityLoad(BlockEntity blockEntity, ServerWorld world) {
        PathService.INSTANCE.onNIOBaseBlockEntityLoad(blockEntity, world);
        ConnectService.INSTANCE.onNepCableEntityLoad(blockEntity, world);
    }

    private void onBlockEntityUnload(BlockEntity blockEntity, ServerWorld world) {
        PathService.INSTANCE.onNIOBaseBlockEntityUnload(blockEntity, world);
        ConnectService.INSTANCE.onNepCableEntityUnload(blockEntity, world);
    }

    private void onServerEndTick(MinecraftServer server) {
        PathService.INSTANCE.pathFinding();
        ConnectService.INSTANCE.connectUpdating();
        TransferService.ITEM_INSTANCE.doTransfer();
    }

}
