package net.infstudio.nepio.network.api.upgrade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.screen.RedstoneUpgradeScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class RedstoneUpgrade implements IUpgrade {

    private BlockEntity blockEntity;
    public enum RedstoneType {
        ON, OFF, IGNORE
    }
    private RedstoneType type;

    public RedstoneUpgrade(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.type = RedstoneType.IGNORE;
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
    public ExtendedScreenHandlerFactory createExtendedScreenHandlerFactory(PacketUpgradeScreen packet, IUpgradeEntity upgradeEntity, int index) {
        return new ExtendedScreenHandlerFactory() {

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                packet.toPacket(buf);
            }

            @Override
            public Text getDisplayName() {
                return new TranslatableText("gui.nepio.title.redstone");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new RedstoneUpgradeScreenHandler(syncId, inv, upgradeEntity, index);
            }

        };
    }

    @Override
    public void copyFrom(IUpgrade other) {
        if (other instanceof RedstoneUpgrade o) {
            type = o.type;
        }
    }

    @Override
    public void clean() {
        type = RedstoneType.IGNORE;
    }

    public void setType(RedstoneType type) {
        this.type = type;
    }

    public RedstoneType getType() {
        return type;
    }

    public boolean isEnabled() {
        switch (type) {
            case ON: return blockEntity.getWorld().isReceivingRedstonePower(blockEntity.getPos());
            case OFF: return !blockEntity.getWorld().isReceivingRedstonePower(blockEntity.getPos());
        }
        return true;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        IUpgrade.super.readNbt(nbt);
        type = RedstoneType.values()[nbt.getInt("type")];
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        IUpgrade.super.writeNbt(nbt);
        nbt.putInt("type", type.ordinal());
    }

}
