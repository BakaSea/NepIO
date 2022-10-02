package net.infstudio.nepio.blockentity.part;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.network.api.automation.IInsertable;
import net.infstudio.nepio.registry.NIOItems;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.function.Predicate;

public class InputPortEntity extends IOPortEntity {

    private IInsertable<ItemVariant> insertable;

    public InputPortEntity(NIOBaseBlockEntity blockEntity, Direction direction) {
        super((PartBaseItem) NIOItems.INPUT_PORT.get(), blockEntity, direction);
        insertable = buildInsertable();
    }

    private IInsertable<ItemVariant> buildInsertable() {
        return new IInsertable<>() {

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public Predicate<ItemVariant> getFilter() {
                return itemVariant -> {
                    if (filterUpgrade.getSize() > 0) return filterUpgrade.match(itemVariant);
                    return true;
                };
            }

            @Override
            public Storage<ItemVariant> getStorage() {
                return ItemStorage.SIDED.find(getWorld(), getPos().offset(direction), direction.getOpposite());
            }

            @Override
            public Class<ItemVariant> get() {
                return ItemVariant.class;
            }

        };
    }

    @Override
    public List<IComponent> getComponents() {
        return List.of(insertable);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(NIOItems.INPUT_PORT.get().getTranslationKey());
    }

}
