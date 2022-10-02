package net.infstudio.nepio.screen;

import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;

public class FilterUpgradeScreenHandler extends AbstractUpgradeScreenHandler {

    private Inventory bank;

    public FilterUpgradeScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(buf.readInt()), new SimpleInventory(buf.readInt()));
    }

    public FilterUpgradeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory bank, Inventory upgrades) {
        super(NIOScreenHandlers.FILTER_UPGRADE_SCREEN_HANDLER, syncId, playerInventory, upgrades);
        addGuiSlot(bank);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return bank.canPlayerUse(player);
    }

    public Inventory getBank() {
        return bank;
    }

}
