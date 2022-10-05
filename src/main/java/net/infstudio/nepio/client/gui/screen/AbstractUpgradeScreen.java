package net.infstudio.nepio.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.infstudio.nepio.NepIO;
import net.infstudio.nepio.client.gui.widget.ItemTab;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.registry.NIONetworkHandlers;
import net.infstudio.nepio.screen.AbstractUpgradeScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Use with {@link AbstractUpgradeScreenHandler}.
 */
public abstract class AbstractUpgradeScreen<T extends AbstractUpgradeScreenHandler> extends HandledScreen<T> {

    private static final Identifier TEXTURE = new Identifier(NepIO.MODID, "textures/gui/screen/upgrade_screen.png");

    private List<ItemTab> itemTabs;

    public AbstractUpgradeScreen(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        itemTabs = new ArrayList<>();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width-backgroundWidth)/2;
        int y = (height-backgroundHeight)/2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        drawTab(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth-textRenderer.getWidth(title))/2;
        initTabs();
    }

    protected void initTabs() {
        handler.initTabs();
        itemTabs.clear();
        var tabs = handler.getTabs();
        for (int i = 0; i < tabs.size(); ++i) {
            int finalI = i;
            itemTabs.add(new ItemTab(x+i*28, y-30, button -> {
                PacketUpgradeScreen packet = handler.getPacket().copy();
                packet.setResult(PacketUpgradeScreen.PacketResult.CHANGE);
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("index", tabs.get(finalI).getRight());
                packet.setNbt(nbt);
                PacketByteBuf buf = PacketByteBufs.create();
                packet.toPacket(buf);
                ClientPlayNetworking.send(NIONetworkHandlers.UPGRADE_SCREEN_PACKET, buf);
            }, new ItemStack(tabs.get(i).getLeft()), tabs.get(i).getRight() == handler.getIndex(), itemRenderer));
        }
    }

    protected void drawTab(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (ItemTab tab : itemTabs) {
            tab.render(matrices, mouseX, mouseY, delta);
        }
    }

    protected void drawGuiSlot(MatrixStack matrices, Inventory inventory) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int sx = (int) (backgroundWidth/2-(float) inventory.size()/2.0F*18.0F)+x;
        int sy = 34+y;
        for (int i = 0; i < inventory.size(); ++i) {
            drawTexture(matrices, sx+i*18, sy, 176, 0, 18, 18);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ItemTab tab : itemTabs) {
            if (tab.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void syncUpgradeEntity() {
        PacketUpgradeScreen packet = handler.getPacket().copy();
        packet.setResult(PacketUpgradeScreen.PacketResult.SYNC);
        NbtCompound nbt = new NbtCompound();
        handler.getUpgradeEntity().writeNbt(nbt);
        packet.setNbt(nbt);
        PacketByteBuf buf = PacketByteBufs.create();
        packet.toPacket(buf);
        ClientPlayNetworking.send(NIONetworkHandlers.UPGRADE_SCREEN_PACKET, buf);
    }

}
