package net.infstudio.nepio.client.network;

import net.minecraft.network.PacketByteBuf;

public interface IPacket {

    void fromPacket(PacketByteBuf buf);

    void toPacket(PacketByteBuf buf);

    void executeOnClient();

    void executeOnServer();

}
