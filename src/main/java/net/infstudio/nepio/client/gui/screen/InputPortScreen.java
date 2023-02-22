package net.infstudio.nepio.client.gui.screen;

import net.infstudio.nepio.network.api.automation.IInsertable;
import net.infstudio.nepio.network.api.upgrade.DistributionUpgrade;
import net.infstudio.nepio.screen.InputPortScreenHandler;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class InputPortScreen extends IOPortScreen<InputPortScreenHandler> {

    public InputPortScreen(InputPortScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(new ButtonWidget(x+(backgroundWidth-80)/2, y+56, 80, 20,
            new LiteralText(handler.getDistributionUpgrade().getMode().name()), button -> {
            DistributionUpgrade distributionUpgrade = handler.getDistributionUpgrade();
            distributionUpgrade.setMode(IInsertable.Mode.values()[(distributionUpgrade.getMode().ordinal()+1)%IInsertable.Mode.values().length]);
            syncUpgradeEntity();
        }) {
            @Override
            public Text getMessage() {
                return new LiteralText(handler.getDistributionUpgrade().getMode().name());
            }
        });
    }

}
