package net.infstudio.nepio.client.network;

import net.minecraft.server.network.ServerPlayerEntity;

public interface IExecutableServer {

    void executeOnServer(ServerPlayerEntity player);

}
