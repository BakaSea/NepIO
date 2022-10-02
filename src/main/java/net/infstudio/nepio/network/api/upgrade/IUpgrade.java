package net.infstudio.nepio.network.api.upgrade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.infstudio.nepio.network.api.IComponent;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;

public interface IUpgrade extends IComponent {

    default ExtendedScreenHandlerFactory createExtendedScreenHandlerFactory(PacketByteBuf buffer, Inventory upgrades) {
        return null;
    }

    void markDirty();

}
