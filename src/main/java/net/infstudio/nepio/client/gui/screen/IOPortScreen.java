package net.infstudio.nepio.client.gui.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.item.upgrade.IUpgradeItem;
import net.infstudio.nepio.registry.NIONetworkHandlers;
import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class IOPortScreen extends AbstractUpgradeScreen<IOPortScreenHandler> {

    private Item[] preUpgrades;

    public IOPortScreen(IOPortScreenHandler handler, PlayerInventory inventory, Text title) {
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
            PacketUpgradeScreen packet = handler.getPacket().copy();
            packet.setResult(PacketUpgradeScreen.PacketResult.UPGRADE);
            NbtCompound nbt = new NbtCompound();
            handler.getUpgradeEntity().writeNbt(nbt);
            packet.setNbt(nbt);
            PacketByteBuf buf = PacketByteBufs.create();
            packet.toPacket(buf);
            ClientPlayNetworking.send(NIONetworkHandlers.UPGRADE_SCREEN_PACKET, buf);
            initTabs();
        }
    }

}
