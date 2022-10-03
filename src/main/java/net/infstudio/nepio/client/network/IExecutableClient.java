package net.infstudio.nepio.client.network;

import net.minecraft.client.network.ClientPlayerEntity;

public interface IExecutableClient {

    void executeOnClient(ClientPlayerEntity player);

}
