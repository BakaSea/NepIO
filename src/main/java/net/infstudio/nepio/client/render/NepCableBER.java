package net.infstudio.nepio.client.render;

import net.infstudio.nepio.blockentity.NepCableEntity;
import net.infstudio.nepio.blockentity.part.PartBaseEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class NepCableBER implements BlockEntityRenderer<NepCableEntity> {

    public NepCableBER(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(NepCableEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        for (var entry : entity.getPartMap().entrySet()) {
            Direction direction = entry.getKey();
            PartBaseEntity partEntity = entry.getValue();
            if (partEntity != null) {
                matrices.push();
                matrices.translate(0.5D, 0.5D, 0.5D);
                Quaternion quaternion = null;
                switch (direction) {
                    case NORTH -> quaternion = Quaternion.IDENTITY;
                    case EAST -> quaternion = Vec3f.NEGATIVE_Y.getDegreesQuaternion(90.0F);
                    case SOUTH -> quaternion = Vec3f.NEGATIVE_Y.getDegreesQuaternion(180.0F);
                    case WEST -> quaternion = Vec3f.NEGATIVE_Y.getDegreesQuaternion(270.0F);
                    case UP -> quaternion = Vec3f.NEGATIVE_X.getDegreesQuaternion(270.0F);
                    case DOWN -> quaternion = Vec3f.NEGATIVE_X.getDegreesQuaternion(90.0F);
                }
                matrices.multiply(quaternion);
                MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(partEntity.getItem()), ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
                matrices.pop();
            }
        }
    }

}
