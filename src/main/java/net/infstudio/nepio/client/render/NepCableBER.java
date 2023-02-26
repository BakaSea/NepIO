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
                Quaternion quaternion = direction.getAxis().isHorizontal() ?
                    Vec3f.POSITIVE_Y.getDegreesQuaternion((2-direction.getHorizontal())*90F) :
                    Vec3f.POSITIVE_X.getDegreesQuaternion((2*direction.getId()-1)*90F);
                matrices.multiply(quaternion);
                MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(partEntity.getItem()), ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
                matrices.pop();
            }
        }
    }

}
