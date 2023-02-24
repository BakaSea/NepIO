package net.infstudio.nepio.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.infstudio.nepio.blockentity.TankEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TankBlock extends AbstractStorageBlock {

    public TankBlock(int level) {
        super(level, FabricBlockSettings.of(Material.GLASS).nonOpaque());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TankEntity(pos, state);
    }

    public static long getCapacity(int level) {
        return (16L << ((level-1) << 1))*FluidConstants.BUCKET;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof TankEntity tankEntity) {
            if (tankEntity.insert(player, hand)) {
                return ActionResult.success(world.isClient);
            }
            if (tankEntity.extract(player, hand)) {
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.FAIL;
    }

}
