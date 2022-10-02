package net.infstudio.nepio.client.gui.screen;

import net.infstudio.nepio.screen.FilterUpgradeScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class FilterUpgradeScreen extends AbstractUpgradeScreen<FilterUpgradeScreenHandler> {

    public FilterUpgradeScreen(FilterUpgradeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        super.drawBackground(matrices, delta, mouseX, mouseY);
        drawGuiSlot(matrices, handler.getBank());
    }

}
