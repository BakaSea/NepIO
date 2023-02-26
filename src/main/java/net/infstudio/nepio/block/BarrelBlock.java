package net.infstudio.nepio.block;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.infstudio.nepio.blockentity.BarrelEntity;
import net.infstudio.nepio.mixin.ClientPlayerInteractionManagerAccessor;
import net.infstudio.nepio.registry.NIOBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BarrelBlock extends AbstractStorageBlock<ItemVariant> {

    public BarrelBlock(int level) {
        super(level, NIOBlocks.getDefaultSettings());
        setDefaultState(getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public long getCapacity(ItemVariant variant) {
        return (32L << ((level-1) << 1))*variant.getItem().getMaxCount();
    }

    public static ActionResult insert(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (world.getBlockEntity(pos) instanceof BarrelEntity entity && hitResult.getSide() == world.getBlockState(pos).get(Properties.HORIZONTAL_FACING)) {
            if (player.isSneaking()) {
                ItemVariant current = ItemVariant.of(player.getMainHandStack());
                if (StorageUtil.move(PlayerInventoryStorage.of(player), entity.getStorage(),
                    itemVariant -> itemVariant.equals(current), Long.MAX_VALUE, null) > 0) {
                    return ActionResult.success(world.isClient);
                }
            } else {
                if (StorageUtil.move(PlayerInventoryStorage.of(player).getSlots().get(player.getInventory().selectedSlot), entity.getStorage(),
                    f -> true, Long.MAX_VALUE, null) > 0) {
                    return ActionResult.success(world.isClient);
                }
            }
        }
        return ActionResult.PASS;
    }

    public static ActionResult extract(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        if (world.getBlockEntity(pos) instanceof BarrelEntity entity && direction == world.getBlockState(pos).get(Properties.HORIZONTAL_FACING)) {
            SingleVariantStorage<ItemVariant> storage = entity.getStorage();
            if (storage.isResourceBlank()) return ActionResult.PASS;
            try (Transaction transaction = Transaction.openOuter()) {
                ItemVariant itemVariant = storage.variant;
                long extracted = storage.extract(storage.variant, player.isSneaking() ? itemVariant.getItem().getMaxCount() : 1, transaction);
                PlayerInventoryStorage.of(player).offerOrDrop(itemVariant, extracted, transaction);
                transaction.commit();
                if (world.isClient && player == MinecraftClient.getInstance().player) {
                    ((ClientPlayerInteractionManagerAccessor) MinecraftClient.getInstance().interactionManager).setBlockBreakingCooldown(5);
                }
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

}
