package net.infstudio.nepio.blockentity.part;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.network.api.automation.IInsertable;
import net.infstudio.nepio.network.api.upgrade.DistributionUpgrade;
import net.infstudio.nepio.network.api.upgrade.IUpgrade;
import net.infstudio.nepio.registry.NIOItems;
import net.infstudio.nepio.screen.InputPortScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FluidInputPortEntity extends FluidIOPortEntity {

    private IInsertable<FluidVariant> insertable;
    private DistributionUpgrade distributionUpgrade;

    public FluidInputPortEntity(NIOBaseBlockEntity blockEntity, Direction direction) {
        super((PartBaseItem) NIOItems.FLUID_INPUT_PORT.get(), blockEntity, direction);
        this.distributionUpgrade = new DistributionUpgrade(blockEntity);
        this.insertable = buildInsertable();
    }

    private IInsertable<FluidVariant> buildInsertable() {
        return new IInsertable<>() {

            @Override
            public Mode getMode() {
                return distributionUpgrade.getMode();
            }

            @Override
            public boolean isEnabled() {
                return redstoneUpgrade.isEnabled();
            }

            @Override
            public int getPriority() {
                return priorityUpgrade.getPriority();
            }

            @Override
            public int getSpeed() {
                return 8100;
                //return speedUpgrade.getSpeed();
            }

            @Override
            public Predicate<FluidVariant> getFilter() {
                return fluidVariant -> {
                    //if (filterUpgrade.getSize() > 0) return filterUpgrade.match(itemVariant);
                    return true;
                };
            }

            @Override
            public Storage<FluidVariant> getStorage() {
                return FluidStorage.SIDED.find(getWorld(), getPos().offset(direction), direction.getOpposite());
            }

            @Override
            public Class<FluidVariant> get() {
                return FluidVariant.class;
            }

        };
    }

    @Override
    public List<IComponent> getComponents() {
        return List.of(insertable);
    }

    @Override
    public <T extends IUpgrade> T getUpgrade(Class<T> upgrade) {
        if (upgrade == DistributionUpgrade.class) return (T) distributionUpgrade;
        return super.getUpgrade(upgrade);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(NIOItems.FLUID_INPUT_PORT.get().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new InputPortScreenHandler(syncId, inv, this, 0);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        distributionUpgrade.readNbt(nbt.getCompound("distribution"));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound distributionNbt = new NbtCompound();
        distributionUpgrade.writeNbt(distributionNbt);
        nbt.put("distribution", distributionNbt);
    }

}
