package net.infstudio.nepio.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.infstudio.nepio.blockentity.NepCableEntity;
import net.infstudio.nepio.item.part.IPartItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NepCable extends NIOBaseBlock {

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final Map<Direction, BooleanProperty> PROPERTY_MAP;
    private static final Map<BlockState, VoxelShape> SHAPE_CACHE;

    static {
        PROPERTY_MAP = new HashMap<>();
        PROPERTY_MAP.put(Direction.NORTH, NORTH);
        PROPERTY_MAP.put(Direction.EAST, EAST);
        PROPERTY_MAP.put(Direction.SOUTH, SOUTH);
        PROPERTY_MAP.put(Direction.WEST, WEST);
        PROPERTY_MAP.put(Direction.UP, UP);
        PROPERTY_MAP.put(Direction.DOWN, DOWN);
        SHAPE_CACHE = new HashMap<>();
    }

    public NepCable() {
        super(FabricBlockSettings.of(Material.METAL));
        setDefaultState(getStateManager().getDefaultState()
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(UP, false)
            .with(DOWN, false)
        );
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NepCableEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getAllShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getAllShape(state, world, pos, context);
    }

    public static VoxelShape getStateShape(BlockState state) {
        return SHAPE_CACHE.computeIfAbsent(state, state1 -> {
            VoxelShape core = VoxelShapes.cuboid(0.375D, 0.375D, 0.375D, 0.625D, 0.625D, 0.625D);
            List<VoxelShape> sides = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                if (state1.get(PROPERTY_MAP.get(direction))) {
                    double[] mins = new double[]{0.375D, 0.375D, 0.375D};
                    double[] maxs = new double[]{0.625D, 0.625D, 0.625D};
                    int axis = direction.getAxis().ordinal();
                    if (direction.getDirection().equals(Direction.AxisDirection.POSITIVE)) {
                        maxs[axis] = 1;
                    } else {
                        mins[axis] = 0;
                    }
                    sides.add(VoxelShapes.cuboid(mins[0], mins[1], mins[2], maxs[0], maxs[1], maxs[2]));
                }
            }
            return VoxelShapes.union(core, sides.toArray(new VoxelShape[]{}));
        });
    }

    private static VoxelShape getAllShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos) instanceof NepCableEntity entity) {
            List<VoxelShape> parts = new ArrayList<>();
            for (var entry : entity.getPartMap().entrySet()) {
                parts.add(entry.getValue().getItem().getShape(entry.getKey()));
            }
            return VoxelShapes.union(getStateShape(state), parts.toArray(new VoxelShape[]{}));
        } else return getStateShape(state);
    }

}
