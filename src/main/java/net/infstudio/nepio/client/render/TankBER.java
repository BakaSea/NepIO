package net.infstudio.nepio.client.render;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.infstudio.nepio.blockentity.TankEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.Direction;

public class TankBER implements BlockEntityRenderer<TankEntity> {

    private static final float TANK_W = 0.001f;

    public TankBER(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(TankEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        FluidVariant fluid = entity.getStorage().variant;
        int fluidColor = FluidVariantRendering.getColor(fluid, entity.getWorld(), entity.getPos());
        Sprite sprite = FluidVariantRendering.getSprite(fluid);
        if (sprite == null) return;
        float r = (fluidColor >> 16 & 255)/255f;
        float g = (fluidColor >> 8 & 255)/255f;
        float b = (fluidColor & 255)/255f;
        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE));
        float fill = entity.isCreative() ? 1-TANK_W : TANK_W+(1-2*TANK_W)*entity.getStorage().amount/entity.getStorage().getCapacity();
        float topHeight = fill;
        float bottomHeight = TANK_W;
        if (FluidVariantAttributes.isLighterThanAir(fluid)) {
            topHeight = 1-TANK_W;
            bottomHeight = 1-fill;
        }
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        for (Direction direction : Direction.values()) {
            QuadEmitter emitter = renderer.meshBuilder().getEmitter();
            if (direction.getAxis().isVertical()) {
                emitter.square(direction, TANK_W, TANK_W, 1-TANK_W, 1-TANK_W, direction == Direction.UP ? 1-topHeight : bottomHeight);
            } else {
                emitter.square(direction, TANK_W, bottomHeight, 1-TANK_W, topHeight, TANK_W);
            }
            emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.spriteColor(0, -1, -1, -1, -1);
            vc.quad(matrices.peek(), emitter.toBakedQuad(0, sprite, false), r, g, b, light, overlay);
        }
    }

}
