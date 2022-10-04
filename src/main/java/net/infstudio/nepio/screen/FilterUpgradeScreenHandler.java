package net.infstudio.nepio.screen;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.infstudio.nepio.screen.slot.FilterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.SlotActionType;

public class FilterUpgradeScreenHandler extends AbstractUpgradeScreenHandler {

    private Inventory bank;

    private static Inventory getBankInventory(PacketByteBuf buf) {
        PacketUpgradeScreen packet = new PacketUpgradeScreen(PacketByteBufs.copy(buf));
        return new SimpleInventory(packet.getNbt().getCompound("filter").getInt("size"));
    }

    public FilterUpgradeScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, getBankInventory(buf), getUpgradeEntity(playerInventory.player, buf), getIndex(buf));
        packetUpgradeScreen = new PacketUpgradeScreen(buf);
    }

    public FilterUpgradeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory bank, IUpgradeEntity upgradeEntity, int index) {
        super(NIOScreenHandlers.FILTER_UPGRADE_SCREEN_HANDLER, syncId, playerInventory, upgradeEntity, index);
        this.bank = bank;
        this.bank.onOpen(playerInventory.player);
        addGuiSlot(bank, FilterSlot::new);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return bank.canPlayerUse(player);
    }

    public Inventory getBank() {
        return bank;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex >= 0 && slotIndex < slots.size() && getSlot(slotIndex) instanceof FilterSlot slot) {
            ItemStack stack = getCursorStack();
            if (!slot.getStack().isEmpty() && stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
                slot.markDirty();
            } else if (!stack.isEmpty()) {
                slot.setStack(new ItemStack(stack.getItem()));
                slot.markDirty();
            }
        } else super.onSlotClick(slotIndex, button, actionType, player);
    }

}
