package net.infstudio.nepio.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.infstudio.nepio.client.gui.screen.*;
import net.infstudio.nepio.client.render.NepCableBER;
import net.infstudio.nepio.client.render.TankBER;
import net.infstudio.nepio.registry.NIOBlockEntities;
import net.infstudio.nepio.registry.NIOBlocks;
import net.infstudio.nepio.registry.NIOItems;
import net.infstudio.nepio.registry.NIOScreenHandlers;
import net.infstudio.nepio.screen.IOPortScreenHandler;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class NepIOClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
            NIOBlocks.TANK_1.get(), NIOBlocks.TANK_2.get(), NIOBlocks.TANK_3.get(), NIOBlocks.TANK_4.get(), NIOBlocks.TANK_5.get());

        BlockEntityRendererRegistry.register(NIOBlockEntities.NEP_CABLE_ENTITY, NepCableBER::new);
        BlockEntityRendererRegistry.register(NIOBlockEntities.TANK_ENTITY, TankBER::new);

        BuiltinItemRendererRegistry.INSTANCE.register(NIOItems.TANK_1.get(), NepIOClient::renderItemBlockEntity);
        BuiltinItemRendererRegistry.INSTANCE.register(NIOItems.TANK_2.get(), NepIOClient::renderItemBlockEntity);
        BuiltinItemRendererRegistry.INSTANCE.register(NIOItems.TANK_3.get(), NepIOClient::renderItemBlockEntity);
        BuiltinItemRendererRegistry.INSTANCE.register(NIOItems.TANK_4.get(), NepIOClient::renderItemBlockEntity);
        BuiltinItemRendererRegistry.INSTANCE.register(NIOItems.TANK_5.get(), NepIOClient::renderItemBlockEntity);

        HandledScreens.register(NIOScreenHandlers.IO_PORT_SCREEN_HANDLER, IOPortScreen<IOPortScreenHandler>::new);
        HandledScreens.register(NIOScreenHandlers.INPUT_PORT_SCREEN_HANDLER, InputPortScreen::new);
        HandledScreens.register(NIOScreenHandlers.FILTER_UPGRADE_SCREEN_HANDLER, FilterUpgradeScreen::new);
        HandledScreens.register(NIOScreenHandlers.PRIORITY_UPGRADE_SCREEN_HANDLER, PriorityUpgradeScreen::new);
        HandledScreens.register(NIOScreenHandlers.REDSTONE_UPGRADE_SCREEN_HANDLER, RedstoneUpgradeScreen::new);
    }

    private static void renderItemBlockEntity(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices,
                                              VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            throw new IllegalArgumentException("Stack must be a block item!");
        }
        if (!(blockItem.getBlock() instanceof BlockEntityProvider blockEntityProvider)) {
            throw new IllegalArgumentException("Block must be an entity block!");
        }
        BlockEntity blockEntity = blockEntityProvider.createBlockEntity(BlockPos.ORIGIN, blockItem.getBlock().getDefaultState());
        NbtCompound compound = Objects.requireNonNullElseGet(BlockItem.getBlockEntityNbt(stack), NbtCompound::new);
        Objects.requireNonNull(blockEntity).readNbt(compound);
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockEntity.getCachedState(), matrices, vertexConsumers, light, overlay);
        BlockEntityRenderer<BlockEntity> ber = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(blockEntity);
        Objects.requireNonNull(ber).render(blockEntity, MinecraftClient.getInstance().getTickDelta(), matrices, vertexConsumers, light, overlay);
    }

}
