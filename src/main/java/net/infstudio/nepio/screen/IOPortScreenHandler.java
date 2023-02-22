package net.infstudio.nepio.screen;

import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.infstudio.nepio.screen.slot.ValidSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class IOPortScreenHandler extends AbstractUpgradeScreenHandler {

    public IOPortScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, getUpgradeEntity(playerInventory.player, buf), getIndex(buf));
        packetUpgradeScreen = new PacketUpgradeScreen(buf);
    }

    public IOPortScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, IUpgradeEntity upgradeEntity, int index) {
        super(type, syncId, playerInventory, upgradeEntity, index);
    }

    public IOPortScreenHandler(int syncId, PlayerInventory playerInventory, IUpgradeEntity upgradeEntity, int index) {
        super(NIOScreenHandlers.IO_PORT_SCREEN_HANDLER, syncId, playerInventory, upgradeEntity, index);
        addGuiSlot(upgradeEntity.getUpgrades(), ValidSlot::new);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return upgradeEntity.getUpgrades().canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < upgradeEntity.getUpgrades().size()) {
                if (!insertItem(originalStack, upgradeEntity.getUpgrades().size(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(originalStack, 0, upgradeEntity.getUpgrades().size(), false)) {
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
