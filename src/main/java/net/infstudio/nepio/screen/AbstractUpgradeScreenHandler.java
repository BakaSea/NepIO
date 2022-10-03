package net.infstudio.nepio.screen;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.infstudio.nepio.blockentity.NepCableEntity;
import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUpgradeScreenHandler extends ScreenHandler {

    protected IUpgradeEntity upgradeEntity;
    protected List<Pair<Item, Integer>> tabs;
    protected int index;
    protected PacketUpgradeScreen packetUpgradeScreen;

    protected static IUpgradeEntity getUpgradeEntity(PlayerEntity player, PacketByteBuf buf) {
        PacketUpgradeScreen packet = new PacketUpgradeScreen(PacketByteBufs.copy(buf));
        BlockEntity blockEntity = player.world.getBlockEntity(packet.getPos());
        if (blockEntity instanceof NepCableEntity nepCable) {
            if (nepCable.getPart(packet.getDirection()) instanceof IUpgradeEntity entity) {
                return entity;
            }
        }
        return null;
    }

    protected static int getIndex(PacketByteBuf buf) {
        PacketUpgradeScreen packet = new PacketUpgradeScreen(new PacketByteBuf(buf.copy()));
        return packet.getIndex();
    }

    public AbstractUpgradeScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, IUpgradeEntity upgradeEntity, int index) {
        super(type, syncId);
        this.upgradeEntity = upgradeEntity;
        this.index = index;
        this.tabs = new ArrayList<>();
        addPlayerInventorySlot(playerInventory);
    }

    protected void addPlayerInventorySlot(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, i*9+j+9, j*18+8, i*18+84));
            }
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, i*18+8, 142));
        }
    }

    protected void addGuiSlot(Inventory inventory, SupplierSlot<?> supplier) {
        int x = (int) (176.0F/2.0F-(float) inventory.size()/2.0F*18.0F)+1;
        int y = 34+1;
        for (int i = 0; i < inventory.size(); ++i) {
            addSlot(supplier.get(inventory, i, x+i*18, y));
        }
    }

    public Inventory getUpgrades() {
        return upgradeEntity.getUpgrades();
    }

    public void initTabs() {
        tabs.clear();
        tabs.add(new Pair<>(upgradeEntity.getItem(), 0));
        Inventory upgrades = getUpgrades();
        for (int i = 0; i < upgrades.size(); ++i) {
            IUpgrade upgrade = upgradeEntity.getUpgrade(i);
            if (upgrade != null && upgrade.hasScreen() && !upgrades.getStack(i).isEmpty()) {
                tabs.add(new Pair<>(upgrades.getStack(i).getItem(), i+1));
            }
        }
    }

    public List<Pair<Item, Integer>> getTabs() {
        return tabs;
    }

    public int getIndex() {
        return index;
    }

    public PacketUpgradeScreen getPacket() {
        return packetUpgradeScreen;
    }

    @FunctionalInterface
    protected interface SupplierSlot<T extends Slot> {

        T get(Inventory inventory, int index, int x, int y);

    }

}
