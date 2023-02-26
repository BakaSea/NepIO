package net.infstudio.nepio.client.render;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.infstudio.nepio.blockentity.BarrelEntity;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class BarrelBER implements BlockEntityRenderer<BarrelEntity> {

    public BarrelBER(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(BarrelEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.hasWorld()) return;
        BlockPos pos = entity.getPos();
        Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
        if (!Block.shouldDrawSide(entity.getCachedState(), entity.getWorld(), pos, direction, pos.offset(direction))) return;
        ItemVariant item = entity.getStorage().variant;
        if (item.isBlank()) return;
        ItemStack stack = item.toStack();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-direction.getHorizontal()*90));
        matrices.scale(0.5F, 0.5F, 0.5F);
        matrices.translate(0, 1.125, 1.01);
        matrices.multiplyPositionMatrix(Matrix4f.scale(1, 1, 0.01F));
        matrices.peek().getNormalMatrix().multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(45F));
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GUI, 0x00F0_00F0, overlay, matrices, vertexConsumers, 0);
        matrices.pop();

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((2-direction.getHorizontal())*90));
        matrices.translate(0, 0.0875, -0.505);
        matrices.scale(-0.01F, -0.01F, -0.01F);
        String amount = String.valueOf(entity.getStorage().amount);
        float x = -textRenderer.getWidth(amount)/2F;
        textRenderer.draw(amount, x, -4F+40, 0x000000, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0, light);
        matrices.pop();
    }

}
