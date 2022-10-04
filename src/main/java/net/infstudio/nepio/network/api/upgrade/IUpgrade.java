package net.infstudio.nepio.network.api.upgrade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.network.api.IComponent;

public interface IUpgrade extends IComponent {

    default ExtendedScreenHandlerFactory createExtendedScreenHandlerFactory(PacketUpgradeScreen packet, IUpgradeEntity upgradeEntity, int index) {
        return null;
    }

    void markDirty();

    boolean hasScreen();

    void copyFrom(IUpgrade other);

    void clean();

}
