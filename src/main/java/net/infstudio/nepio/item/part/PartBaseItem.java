package net.infstudio.nepio.item.part;

import com.mojang.logging.LogUtils;
import net.infstudio.nepio.blockentity.NepCableEntity;
import net.infstudio.nepio.item.BaseItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class PartBaseItem extends BaseItem implements IPartItem {

    private static final Logger LOGGER = LogUtils.getLogger();

    public PartBaseItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        if (world.getBlockEntity(blockPos) instanceof NepCableEntity entity) {
            Vec3d point = context.getHitPos().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ())
                    .subtract(0.5d, 0.5d, 0.5d);
            Direction direction = Direction.getFacing(point.x, point.y, point.z);
            if (!entity.existPart(direction)) {
                if (!world.isClient()) {
                    entity.addPart(direction, createPartEntity(entity, direction));
                    if (!context.getPlayer().isCreative()) {
                        context.getStack().decrement(1);
                    }
                    BlockState state = world.getBlockState(blockPos);
                    world.updateListeners(blockPos, state, state, Block.NOTIFY_LISTENERS);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    protected static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        List<Box> boxes = shape.getBoundingBoxes();
        Quaternion quaternion = null;
        switch (direction) {
            case NORTH -> quaternion = Quaternion.IDENTITY;
            case EAST -> quaternion = Vec3f.NEGATIVE_Y.getDegreesQuaternion(90.0F);
            case SOUTH -> quaternion = Vec3f.NEGATIVE_Y.getDegreesQuaternion(180.0F);
            case WEST -> quaternion = Vec3f.NEGATIVE_Y.getDegreesQuaternion(270.0F);
            case UP -> quaternion = Vec3f.NEGATIVE_X.getDegreesQuaternion(270.0F);
            case DOWN -> quaternion = Vec3f.NEGATIVE_X.getDegreesQuaternion(90.0F);
        }
        List<VoxelShape> shapeList = new ArrayList<>();
        for (Box box : boxes) {
            Vector4f a = new Vector4f((float) box.minX, (float) box.minY, (float) box.minZ, 1);
            Vector4f b = new Vector4f((float) box.maxX, (float) box.maxY, (float) box.maxZ, 1);
            a.add(-0.5F, -0.5F, -0.5F, 0);
            b.add(-0.5F, -0.5F, -0.5F, 0);
            a.rotate(quaternion);
            b.rotate(quaternion);
            Vec3d c = new Vec3d(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
            Vec3d d = new Vec3d(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
            c = c.add(0.5D, 0.5D, 0.5D);
            d = d.add(0.5D, 0.5D, 0.5D);
            shapeList.add(VoxelShapes.cuboid(c.x, c.y, c.z, d.x, d.y, d.z));
        }
        VoxelShape first = shapeList.get(0);
        shapeList.remove(0);
        return VoxelShapes.union(first, shapeList.toArray(new VoxelShape[]{}));
    }

}
