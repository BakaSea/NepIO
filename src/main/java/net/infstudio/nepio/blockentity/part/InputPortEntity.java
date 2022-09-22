package net.infstudio.nepio.blockentity.part;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.network.api.automation.IInsertable;
import net.infstudio.nepio.registry.NIOItems;
import net.minecraft.util.math.Direction;

import java.util.function.Predicate;

public class InputPortEntity extends PartBaseEntity {

    private IInsertable<ItemVariant> insertable;

    public InputPortEntity(NIOBaseBlockEntity blockEntity, Direction direction) {
        super((PartBaseItem) NIOItems.INPUT_PORT.get(), blockEntity, direction);
        insertable = buildInsertable();
    }

    @Override
    public void setNetworkNode(NNetworkNode networkNode) {
        networkNode.addComponent(insertable);
    }

    private IInsertable<ItemVariant> buildInsertable() {
        return new IInsertable<ItemVariant>() {

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
                return itemVariant -> true;
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
    public void onRemove() {
        if (!getWorld().isClient()) {
            getNetworkNode().removeComponent(insertable);
        }
    }

}
