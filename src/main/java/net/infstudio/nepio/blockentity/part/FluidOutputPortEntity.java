package net.infstudio.nepio.blockentity.part;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.network.api.automation.IExtractable;
import net.infstudio.nepio.registry.NIOItems;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.function.Predicate;

public class FluidOutputPortEntity extends FluidIOPortEntity {

    private IExtractable<FluidVariant> extractable;

    public FluidOutputPortEntity(NIOBaseBlockEntity blockEntity, Direction direction) {
        super((PartBaseItem) NIOItems.FLUID_OUTPUT_PORT.get(), blockEntity, direction);
        extractable = buildExtractable();
    }

    private IExtractable<FluidVariant> buildExtractable() {
        return new IExtractable<>() {

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
        return List.of(extractable);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(NIOItems.FLUID_OUTPUT_PORT.get().getTranslationKey());
    }

}
