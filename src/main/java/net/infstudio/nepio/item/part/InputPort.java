package net.infstudio.nepio.item.part;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.infstudio.nepio.blockentity.NIOBaseBlockEntity;
import net.infstudio.nepio.blockentity.part.InputPortEntity;
import net.infstudio.nepio.blockentity.part.PartBaseEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.HashMap;
import java.util.Map;

public class InputPort extends PartBaseItem {

    private static final VoxelShape shape = VoxelShapes.union(
        VoxelShapes.cuboid(0.125D, 0.125D, 0, 0.875D, 0.875D, 0.125D),
        VoxelShapes.cuboid(0.25D, 0.25D, 0.125D, 0.75D, 0.75D, 0.25D),
        VoxelShapes.cuboid(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 0.375D)
    );
    private static final Map<Direction, VoxelShape> SHAPE_CACHE = new HashMap<>();

    public InputPort() {
        super(new FabricItemSettings());
    }

    @Override
    public PartBaseEntity createPartEntity(NIOBaseBlockEntity blockEntity) {
        return new InputPortEntity(blockEntity);
    }

    @Override
    public VoxelShape getShape(Direction direction) {
        return SHAPE_CACHE.computeIfAbsent(direction, direction1 -> rotateShape(shape, direction1));
    }

}
