package net.infstudio.nepio.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractUpgradeScreenHandler extends ScreenHandler {

    protected Inventory upgrades;

    public AbstractUpgradeScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory upgrades) {
        super(type, syncId);
        this.upgrades = upgrades;
        this.upgrades.onOpen(playerInventory.player);
        addPlayerInventorySlot(playerInventory);
    }

    public Inventory getUpgrades() {
        return upgrades;
    }

    protected void addPlayerInventorySlot(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, i*9+j+9, j*18+8, i*18+84));
            }
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, i*18+8, 142));
        }
    }

    protected void addGuiSlot(Inventory inventory) {
        int x = (int) (176.0F/2.0F-(float) inventory.size()/2.0F*18.0F)+1;
        int y = 34+1;
        for (int i = 0; i < inventory.size(); ++i) {
            int finalI = i;
            addSlot(new Slot(inventory, finalI, x, y) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return inventory.isValid(finalI, stack);
                }
            });
            x += 18;
        }
    }

}
