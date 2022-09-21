package net.infstudio.nepio.blockentity.part;

import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.item.part.PartBaseItem;
import net.infstudio.nepio.registry.NIOItems;

public class InputPortEntity extends PartBaseEntity {

    public InputPortEntity(NIOBaseBlockEntity blockEntity) {
        super((PartBaseItem) NIOItems.IMPORT_PORT.get(), blockEntity);
    }

}
