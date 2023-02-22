package net.infstudio.nepio.screen;

import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.network.api.upgrade.DistributionUpgrade;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.infstudio.nepio.screen.slot.ValidSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;

public class InputPortScreenHandler extends IOPortScreenHandler {

    public InputPortScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(syncId, playerInventory, buf);
    }

    public InputPortScreenHandler(int syncId, PlayerInventory playerInventory, IUpgradeEntity upgradeEntity, int index) {
        super(NIOScreenHandlers.INPUT_PORT_SCREEN_HANDLER, syncId, playerInventory, upgradeEntity, index);
        addGuiSlot(upgradeEntity.getUpgrades(), ValidSlot::new);
    }

    public DistributionUpgrade getDistributionUpgrade() {
        return upgradeEntity.getUpgrade(DistributionUpgrade.class);
    }

}
