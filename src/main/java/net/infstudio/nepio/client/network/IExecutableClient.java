package net.infstudio.nepio.client.network;

import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Use with {@link IPacket}.
 * Deserialize the packet from the server and execute on the client.
 */
public interface IExecutableClient {

    void executeOnClient(ClientPlayerEntity player);

}
