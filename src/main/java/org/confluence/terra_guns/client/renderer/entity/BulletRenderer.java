package org.confluence.terra_guns.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

public class BulletRenderer extends EntityRenderer<BaseBulletEntity> {

    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(BaseBulletEntity baseBulletEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(BaseBulletEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        List<Vec3> trails = entity.getTrails();
        if (trails.isEmpty()) return;
        Vec3 pos0;
        Vec3 pos1;
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
        Matrix4f matrix4f = poseStack.last().pose();

        VertexConsumer bufferbuilder = bufferSource.getBuffer(RenderType.lightning());
        for (int i = 1; i < trails.size(); i++) {
            pos0 = trails.get(i - 1).subtract(entity.position());
            pos1 = trails.get(i).subtract(entity.position());

            double x1 = pos0.x;
            double y1 = pos0.y;
            double z1 = pos0.z;
            double x2 = pos1.x;
            double y2 = pos1.y;
            double z2 = pos1.z;
            float width0 = 0.3f / trails.size() * (i - 1);
            float width1 = 0.3f / trails.size() * i;
            bufferbuilder.addVertex(matrix4f, (float) x1, (float) y1, (float) z1 - width0)
                    .setColor(1, 1, 1, 0.5f);
            bufferbuilder.addVertex(matrix4f, (float) x1, (float) y1, (float) z1 + width0)
                    .setColor(1, 1, 1, 0.5f);
            bufferbuilder.addVertex(matrix4f, (float) x2, (float) y2, (float) z2 + width1)
                    .setColor(1, 1, 1, 0.5f);
            bufferbuilder.addVertex(matrix4f, (float) x2, (float) y2, (float) z2 - width1)
                    .setColor(1, 1, 1, 0.5f);

//            bufferbuilder.addVertex(matrix4f, (float) x1 - width0, (float) y1, (float) z1)
//                    .setColor(1, 1, 1, 0.5f);
//            bufferbuilder.addVertex(matrix4f, (float) x1 + width0, (float) y1, (float) z1)
//                    .setColor(1, 1, 1, 0.5f);
//            bufferbuilder.addVertex(matrix4f, (float) x2 + width1, (float) y2, (float) z2)
//                    .setColor(1, 1, 1, 0.5f);
//            bufferbuilder.addVertex(matrix4f, (float) x2 - width1, (float) y2, (float) z2)
//                    .setColor(1, 1, 1, 0.5f);
        }
        poseStack.popPose();

    }
}
