package net.infstudio.nepio.client.gui.screen;

import net.infstudio.nepio.item.upgrade.IUpgradeItem;
import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class IOPortScreen<T extends IOPortScreenHandler> extends AbstractUpgradeScreen<T> {

    private Item[] preUpgrades;

    public IOPortScreen(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        Inventory upgrades = handler.getUpgrades();
        preUpgrades = new Item[upgrades.size()];
        for (int i = 0; i < upgrades.size(); ++i) {
            preUpgrades[i] = upgrades.getStack(i).getItem();
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        super.drawBackground(matrices, delta, mouseX, mouseY);
        drawGuiSlot(matrices, handler.getUpgrades());
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        Inventory upgrades = handler.getUpgrades();
        boolean flag = false;
        for (int i = 0; i < upgrades.size(); ++i) {
            Item curItem = upgrades.getStack(i).getItem();
            if (preUpgrades[i] != curItem) {
                flag = true;
                if (curItem != Items.AIR) {
                    handler.getUpgradeEntity().setUpgrade(i, ((IUpgradeItem) curItem).createUpgradeComponent());
                } else {
                    handler.getUpgradeEntity().removeUpgrade(i);
                }
            }
            preUpgrades[i] = curItem;
        }
        if (flag) {
            syncUpgradeEntity();
            initTabs();
        }
    }

}
