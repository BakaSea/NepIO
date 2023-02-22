package net.infstudio.nepio.network.api.upgrade;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.infstudio.nepio.blockentity.part.IUpgradeEntity;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.screen.PriorityUpgradeScreenHandler;
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

public class PriorityUpgrade implements IUpgrade {

    private int priority;
    private BlockEntity blockEntity;

    public PriorityUpgrade(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.priority = 0;
    }

    public int getPriority() {
        return priority;
    }

    public void inc(int amount) {
        priority += amount;
        if (priority > 100) priority = 100;
    }

    public void dec(int amount) {
        priority -= amount;
        if (priority < -100) priority = -100;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        IUpgrade.super.readNbt(nbt);
        priority = nbt.getInt("priority");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        IUpgrade.super.writeNbt(nbt);
        nbt.putInt("priority", priority);
    }

    @Override
    public void markDirty() {
        blockEntity.markDirty();
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
                return new TranslatableText("gui.nepio.title.priority");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new PriorityUpgradeScreenHandler(syncId, inv, upgradeEntity, index);
            }

        };
    }

    @Override
    public boolean hasScreen() {
        return true;
    }

    @Override
    public void copyFrom(IUpgrade other) {
        if (other instanceof PriorityUpgrade o) {
            priority = o.priority;
        }
    }

    @Override
    public void clean() {
        priority = 0;
    }

}
