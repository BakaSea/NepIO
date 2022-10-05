package net.infstudio.nepio.client.gui.screen;

import net.infstudio.nepio.network.api.upgrade.PriorityUpgrade;
import net.infstudio.nepio.screen.PriorityUpgradeScreenHandler;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class PriorityUpgradeScreen extends AbstractUpgradeScreen<PriorityUpgradeScreenHandler> {

    public PriorityUpgradeScreen(PriorityUpgradeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        String priority = Integer.toString(handler.getPriorityUpgrade().getPriority());
        textRenderer.draw(matrices, priority, (float) (backgroundWidth-textRenderer.getWidth(priority))/2, 34, 0x404040);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(new ButtonWidget(x+115, y+34, 17, 18, new LiteralText("+1"), button -> {
            PriorityUpgrade priorityUpgrade = handler.getPriorityUpgrade();
            priorityUpgrade.inc(1);
            syncUpgradeEntity();
        }));
        addDrawableChild(new ButtonWidget(x+133, y+34, 36, 18, new LiteralText("+10"), button -> {
            PriorityUpgrade priorityUpgrade = handler.getPriorityUpgrade();
            priorityUpgrade.inc(10);
            syncUpgradeEntity();
        }));
        addDrawableChild(new ButtonWidget(x+44, y+34, 17, 18, new LiteralText("-1"), button -> {
            PriorityUpgrade priorityUpgrade = handler.getPriorityUpgrade();
            priorityUpgrade.dec(1);
            syncUpgradeEntity();
        }));
        addDrawableChild(new ButtonWidget(x+7, y+34, 36, 18, new LiteralText("-10"), button -> {
            PriorityUpgrade priorityUpgrade = handler.getPriorityUpgrade();
            priorityUpgrade.dec(10);
            syncUpgradeEntity();
        }));
    }

}
