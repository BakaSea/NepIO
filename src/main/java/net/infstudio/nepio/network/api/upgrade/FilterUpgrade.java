package net.infstudio.nepio.network.api.upgrade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.infstudio.nepio.blockentity.api.BlockEntityInventory;
import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.screen.FilterUpgradeScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class FilterUpgrade implements IUpgrade {

    private BlockEntityInventory bank;
    private boolean whiteList;
    private boolean nbtEnabled;
    private int size;
    private BlockEntity blockEntity;

    public static final FilterUpgrade EMPTY = new FilterUpgrade(0, null);

    public FilterUpgrade(int size, BlockEntity blockEntity) {
        this.size = size;
        this.blockEntity = blockEntity;
        this.bank = new BlockEntityInventory(size, blockEntity);
        this.whiteList = true;
        this.nbtEnabled = false;
    }

    public int getSize() {
        return size;
    }

    public void setWhiteList(boolean whiteList) {
        this.whiteList = whiteList;
    }

    public void changeWhiteList() {
        setWhiteList(!isWhiteList());
    }

    public boolean isWhiteList() {
        return whiteList;
    }

    public void setNbtEnabled(boolean nbtEnabled) {
        this.nbtEnabled = nbtEnabled;
    }

    public void changeNbtEnabled() {
        setNbtEnabled(!isNbtEnabled());
    }

    public boolean isNbtEnabled() {
        return nbtEnabled;
    }

    public BlockEntityInventory getBank() {
        return bank;
    }

    public boolean match(ItemVariant itemVariant) {
        boolean flag = false;
        for (int i = 0; i < bank.size(); ++i) {
            ItemStack stack = bank.getStack(i);
            Item item = stack.getItem();
            if (itemVariant.isOf(item)) {
                if (nbtEnabled) {
                    if (itemVariant.nbtMatches(stack.getNbt())) {
                        flag = true;
                        break;
                    }
                } else {
                    flag = true;
                    break;
                }
            }
        }
        return whiteList == flag;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        IUpgrade.super.readNbt(nbt);
        size = nbt.getInt("size");
        if (size != bank.size()) bank = new BlockEntityInventory(size, blockEntity);
        bank.readNbt(nbt.getCompound("bank"));
        whiteList = nbt.getBoolean("whitelist");
        nbtEnabled = nbt.getBoolean("nbt");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        IUpgrade.super.writeNbt(nbt);
        NbtCompound bankNbt = new NbtCompound();
        bank.writeNbt(bankNbt);
        nbt.put("bank", bankNbt);
        nbt.putInt("size", size);
        nbt.putBoolean("whitelist", whiteList);
        nbt.putBoolean("nbt", nbtEnabled);
    }

    @Override
    public ExtendedScreenHandlerFactory createExtendedScreenHandlerFactory(PacketUpgradeScreen packet, IUpgradeEntity upgradeEntity, int index) {
        return new ExtendedScreenHandlerFactory() {

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                packet.toPacket(buf);
            }

            @Override
            public Text getDisplayName() {
                return new TranslatableText("gui.nepio.title.filter");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new FilterUpgradeScreenHandler(syncId, inv, bank, upgradeEntity, index);
            }

        };
    }

    @Override
    public void markDirty() {
        blockEntity.markDirty();
    }

    @Override
    public boolean hasScreen() {
        return true;
    }

    @Override
    public void copyFrom(IUpgrade other) {
        if (other instanceof FilterUpgrade o) {
            size = o.size;
            whiteList = o.whiteList;
            bank = o.bank;
        }
    }

    @Override
    public void clean() {
        size = 0;
        whiteList = true;
        bank = new BlockEntityInventory(size, blockEntity);
    }

}
