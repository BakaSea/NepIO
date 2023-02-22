package net.infstudio.nepio.blockentity.part;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.blockentity.api.BlockEntityInventory;
import net.infstudio.nepio.client.network.PacketUpgradeScreen;
import net.infstudio.nepio.network.api.upgrade.*;
import net.infstudio.nepio.registry.NIOItems;
import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class IOPortEntity extends PartBaseEntity implements ExtendedScreenHandlerFactory, IUpgradeEntity {

    protected BlockEntityInventory upgrades = new BlockEntityInventory(4, blockEntity) {

        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            switch (slot) {
                case 0: return stack.isOf(NIOItems.FILTER_UPGRADE_BASIC.get())
                        || stack.isOf(NIOItems.FILTER_UPGRADE_ADVANCED.get())
                        || stack.isOf(NIOItems.FILTER_UPGRADE_ULTIMATE.get());
                case 1: return stack.isOf(NIOItems.PRIORITY_UPGRADE.get());
                case 2: return stack.isOf(NIOItems.SPEED_UPGRADE_1.get())
                        || stack.isOf(NIOItems.SPEED_UPGRADE_2.get())
                        || stack.isOf(NIOItems.SPEED_UPGRADE_3.get())
                        || stack.isOf(NIOItems.SPEED_UPGRADE_4.get())
                        || stack.isOf(NIOItems.SPEED_UPGRADE_5.get());
                case 3: return stack.isOf(NIOItems.REDSTONE_UPGRADE.get());
                default: return false;
            }
        }

    };

    protected FilterUpgrade filterUpgrade;
    protected PriorityUpgrade priorityUpgrade;
    protected SpeedUpgrade speedUpgrade;
    protected RedstoneUpgrade redstoneUpgrade;

    public IOPortEntity(PartBaseItem item, NIOBaseBlockEntity blockEntity, Direction direction) {
        super(item, blockEntity, direction);
        filterUpgrade = new FilterUpgrade(0, blockEntity);
        priorityUpgrade = new PriorityUpgrade(blockEntity);
        speedUpgrade = new SpeedUpgrade(0, blockEntity);
        redstoneUpgrade = new RedstoneUpgrade(blockEntity);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new IOPortScreenHandler(syncId, inv, this, 0);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        upgrades.clear();
        upgrades.readNbt(nbt.getCompound("upgrade"));
        filterUpgrade.readNbt(nbt.getCompound("filter"));
        priorityUpgrade.readNbt(nbt.getCompound("priority"));
        speedUpgrade.readNbt(nbt.getCompound("speed"));
        redstoneUpgrade.readNbt(nbt.getCompound("redstone"));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound upgradeNbt = new NbtCompound();
        upgrades.writeNbt(upgradeNbt);
        nbt.put("upgrade", upgradeNbt);
        NbtCompound filterNbt = new NbtCompound();
        filterUpgrade.writeNbt(filterNbt);
        nbt.put("filter", filterNbt);
        NbtCompound priorityNbt = new NbtCompound();
        priorityUpgrade.writeNbt(priorityNbt);
        nbt.put("priority", priorityNbt);
        NbtCompound speedNbt = new NbtCompound();
        speedUpgrade.writeNbt(speedNbt);
        nbt.put("speed", speedNbt);
        NbtCompound redstoneNbt = new NbtCompound();
        redstoneUpgrade.writeNbt(redstoneNbt);
        nbt.put("redstone", redstoneNbt);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        PacketUpgradeScreen packet = new PacketUpgradeScreen(getPos(), direction, 0, upgrades.size(), nbt, PacketUpgradeScreen.PacketResult.COMMON);
        packet.toPacket(buf);
    }

    @Override
    public BlockEntityInventory getUpgrades() {
        return upgrades;
    }

    @Override
    public IUpgrade getUpgrade(int index) {
        switch (index) {
            case 0: return filterUpgrade;
            case 1: return priorityUpgrade;
            case 2: return speedUpgrade;
            case 3: return redstoneUpgrade;
            default: return null;
        }
    }

    @Override
    public <T extends IUpgrade> T getUpgrade(Class<T> upgrade) {
        if (upgrade == FilterUpgrade.class) return (T) filterUpgrade;
        if (upgrade == PriorityUpgrade.class) return (T) priorityUpgrade;
        if (upgrade == SpeedUpgrade.class) return (T) speedUpgrade;
        if (upgrade == RedstoneUpgrade.class) return (T) redstoneUpgrade;
        return null;
    }

    @Override
    public void setUpgrade(int index, IUpgrade upgrade) {
        switch (index) {
            case 0: filterUpgrade.copyFrom(upgrade);
            case 1: priorityUpgrade.copyFrom(upgrade);
            case 2: speedUpgrade.copyFrom(upgrade);
            case 3: redstoneUpgrade.copyFrom(upgrade);
        }
    }

    @Override
    public void removeUpgrade(int index) {
        switch (index) {
            case 0: filterUpgrade.clean();
            case 1: priorityUpgrade.clean();
            case 2: speedUpgrade.clean();
            case 3: redstoneUpgrade.clean();
        }
    }

    @Override
    public void markDirty() {
        blockEntity.markDirty();
    }

    @Override
    public void dropItems(World world, BlockPos pos) {
        super.dropItems(world, pos);
        ItemScatterer.spawn(world, pos, upgrades);
    }

}
