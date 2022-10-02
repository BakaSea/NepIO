package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.infstudio.nepio.NepIO;
import net.infstudio.nepio.blockentity.NepCableEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NIONetworkHandlers implements ModInitializer {

    public static final NIONetworkHandlers INSTANCE = new NIONetworkHandlers();

    public static final Identifier UPGRADE_SCREEN_PACKET = new Identifier(NepIO.MODID, "upgrade_screen_packet");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(UPGRADE_SCREEN_PACKET, this::receiveUpgradeScreenPacket);
    }

    public void receiveUpgradeScreenPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (player.world.getBlockEntity(buf.readBlockPos()) instanceof NepCableEntity nepCableEntity) {

        }
    }

}
