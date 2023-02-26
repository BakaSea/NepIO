package net.infstudio.nepio.item.part;

import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.blockentity.part.OutputPortEntity;
import net.infstudio.nepio.blockentity.part.PartBaseEntity;
import net.infstudio.nepio.registry.NIOItems;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class OutputPort extends PartBaseItem {

    public OutputPort() {
        super(NIOItems.getDefaultSettings());
    }

    @Override
    public PartBaseEntity createPartEntity(NIOBaseBlockEntity blockEntity, Direction direction) {
        return new OutputPortEntity(blockEntity, direction);
    }

    @Override
    public VoxelShape getShape(Direction direction) {
        return ((InputPort) NIOItems.INPUT_PORT.get()).getShape(direction);
    }

}
