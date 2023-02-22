package net.infstudio.nepio.client.gui.screen;

import net.infstudio.nepio.network.api.upgrade.RedstoneUpgrade;
import net.infstudio.nepio.screen.RedstoneUpgradeScreenHandler;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class RedstoneUpgradeScreen extends AbstractUpgradeScreen<RedstoneUpgradeScreenHandler> {

    public RedstoneUpgradeScreen(RedstoneUpgradeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(new ButtonWidget(x+(backgroundWidth-150)/2, y+34, 150, 20, new TranslatableText("gui.nepio.redstone.ignore"), button -> {
            RedstoneUpgrade redstoneUpgrade = handler.getRedstoneUpgrade();
            redstoneUpgrade.setType(RedstoneUpgrade.RedstoneType.values()[(redstoneUpgrade.getType().ordinal()+1)% RedstoneUpgrade.RedstoneType.values().length]);
            syncUpgradeEntity();
        }) {
            @Override
            public Text getMessage() {
                return new TranslatableText("gui.nepio.redstone."+handler.getRedstoneUpgrade().getType().name().toLowerCase());
            }
        });
    }

}
