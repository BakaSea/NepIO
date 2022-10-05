package net.infstudio.nepio.client.gui.screen;

import net.infstudio.nepio.network.api.upgrade.FilterUpgrade;
import net.infstudio.nepio.screen.FilterUpgradeScreenHandler;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
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

    @Override
    protected void init() {
        super.init();
        addDrawableChild(new ButtonWidget(x+7, y+50, 54, 18, null, button -> {
            FilterUpgrade filterUpgrade = handler.getFilterUpgrade();
            filterUpgrade.changeWhiteList();
            syncUpgradeEntity();
        }) {
            private static final Text TEXT_ON = new LiteralText("White list");
            private static final Text TEXT_OFF = new LiteralText("Black list");
            @Override
            public Text getMessage() {
                FilterUpgrade filterUpgrade = handler.getFilterUpgrade();
                return filterUpgrade.isWhiteList() ? TEXT_ON : TEXT_OFF;
            }
        });
        addDrawableChild(new ButtonWidget(x+115, y+50, 54, 18, null, button -> {
            FilterUpgrade filterUpgrade = handler.getFilterUpgrade();
            filterUpgrade.changeNbtEnabled();
            syncUpgradeEntity();
        }) {
            private static final Text TEXT_ON = new LiteralText("Nbt on");
            private static final Text TEXT_OFF = new LiteralText("Nbt off");
            @Override
            public Text getMessage() {
                FilterUpgrade filterUpgrade = handler.getFilterUpgrade();
                return filterUpgrade.isNbtEnabled() ? TEXT_ON : TEXT_OFF;
            }
        });
    }

}
