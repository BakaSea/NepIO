package net.infstudio.nepio.screen;

import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;

public class IOPortScreenHandler extends AbstractUpgradeScreenHandler {

    private static Inventory computeUpgradeSize(PacketByteBuf buf) {
        PacketUpgradeScreen packet = PacketUpgradeScreen.of(new PacketByteBuf((buf.copy())));
        return new SimpleInventory(packet.getUpgradeSize());
    }

    public IOPortScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, computeUpgradeSize(buf));
    }

    public IOPortScreenHandler(int syncId, PlayerInventory playerInventory, Inventory upgrades) {
        super(NIOScreenHandlers.IO_PORT_SCREEN_HANDLER, syncId, playerInventory, upgrades);
        addGuiSlot(upgrades);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return upgrades.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < upgrades.size()) {
                if (!insertItem(originalStack, upgrades.size(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(originalStack, 0, upgrades.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

}
