package net.infstudio.nepio.client.network;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Use with {@link IPacket}.
 * Deserialize the packet from the client and execute on the server.
 */
public interface IExecutableServer {

    void executeOnServer(ServerPlayerEntity player);

}
