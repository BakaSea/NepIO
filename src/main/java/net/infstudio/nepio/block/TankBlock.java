package net.infstudio.nepio.block;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.infstudio.nepio.blockentity.TankEntity;
import net.infstudio.nepio.registry.NIOBlocks;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TankBlock extends AbstractStorageBlock<FluidVariant> {

    public TankBlock(int level) {
        super(level, NIOBlocks.getDefaultSettings().of(Material.GLASS).nonOpaque());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TankEntity(pos, state);
    }

    @Override
    public long getCapacity(FluidVariant variant) {
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
            ItemStack itemStack = null;
            if (player.isCreative()) {
                itemStack = player.getStackInHand(hand).copy();
            }
            Storage<FluidVariant> playerHand = ContainerItemContext.ofPlayerHand(player, hand).find(FluidStorage.ITEM);
            if (StorageUtil.move(playerHand, tankEntity.getStorage(), f -> true, Long.MAX_VALUE, null) > 0) {
                if (player.isCreative() && itemStack != null) {
                    player.setStackInHand(hand, itemStack);
                }
                return ActionResult.success(world.isClient);
            }
            if (StorageUtil.move(tankEntity.getStorage(), playerHand, f -> true, Long.MAX_VALUE, null) > 0) {
                if (player.isCreative() && itemStack != null) {
                    player.setStackInHand(hand, itemStack);
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.FAIL;
    }

}
