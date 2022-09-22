package net.infstudio.nepio.item.part;

import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.blockentity.part.PartBaseEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public interface IPartItem {

    PartBaseEntity createPartEntity(NIOBaseBlockEntity blockEntity, Direction direction);

    VoxelShape getShape(Direction direction);

}
