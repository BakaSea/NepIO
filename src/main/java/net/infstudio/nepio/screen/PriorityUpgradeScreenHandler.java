package net.infstudio.nepio.screen;

import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.network.api.upgrade.PriorityUpgrade;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;

public class PriorityUpgradeScreenHandler extends AbstractUpgradeScreenHandler {

    public PriorityUpgradeScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, getUpgradeEntity(playerInventory.player, buf), getIndex(buf));
        packetUpgradeScreen = new PacketUpgradeScreen(buf);
    }

    public PriorityUpgradeScreenHandler(int syncId, PlayerInventory playerInventory, IUpgradeEntity upgradeEntity, int index) {
        super(NIOScreenHandlers.PRIORITY_UPGRADE_SCREEN_HANDLER, syncId, playerInventory, upgradeEntity, index);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public PriorityUpgrade getPriorityUpgrade() {
        return upgradeEntity.getUpgrade(PriorityUpgrade.class);
    }

}
