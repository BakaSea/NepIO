package net.infstudio.nepio.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.infstudio.nepio.NepIO;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemTab extends ButtonWidget {

    private ItemStack itemStack;
    private boolean selected;
    private ItemRenderer itemRenderer;
    private static final Identifier TEXTURE = new Identifier(NepIO.MODID, "textures/gui/widget/item_tab.png");

    public ItemTab(int x, int y, PressAction onPress, ItemStack itemStack, boolean selected, ItemRenderer itemRenderer) {
        super(x, y, 28, 30, null, onPress);
        this.itemStack = itemStack;
        this.selected = selected;
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void onPress() {
        if (!selected) super.onPress();
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, selected ? height : 0, width, height);
        itemRenderer.renderInGuiWithOverrides(itemStack, x+6, y+10);
    }

}
