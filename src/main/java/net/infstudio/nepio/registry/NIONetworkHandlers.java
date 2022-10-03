package net.infstudio.nepio.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.infstudio.nepio.NepIO;
import net.infstudio.nepio.client.network.IExecutableServer;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NIONetworkHandlers implements ModInitializer {

    public static final NIONetworkHandlers INSTANCE = new NIONetworkHandlers();

    public static final Identifier UPGRADE_SCREEN_PACKET = new Identifier(NepIO.MODID, "upgrade_screen_packet");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(UPGRADE_SCREEN_PACKET,
            ((server, player, handler, buf, responseSender) -> {
                PacketUpgradeScreen packet = new PacketUpgradeScreen(buf);
                server.execute(() -> executeOnServer(player, packet));
            }));
    }

    public void executeOnServer(ServerPlayerEntity player, IExecutableServer executable) {
        executable.executeOnServer(player);
    }

}
