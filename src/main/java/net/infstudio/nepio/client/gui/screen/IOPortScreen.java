package net.infstudio.nepio.client.gui.screen;

import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class IOPortScreen extends AbstractUpgradeScreen<IOPortScreenHandler> {

    public IOPortScreen(IOPortScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        super.drawBackground(matrices, delta, mouseX, mouseY);
        drawGuiSlot(matrices, handler.getUpgrades());
    }

}
