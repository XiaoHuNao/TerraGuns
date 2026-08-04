package org.confluence.terra_guns.client.renderer.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.client.init.TGRenderTypes;
import org.joml.Matrix4f;

public final class BulletVfxRenderUtil {
    private static final double EPSILON = 1.0E-7D;
    private static final Vec3 UP = new Vec3(0.0D, 1.0D, 0.0D);
    private static final Vec3 RIGHT = new Vec3(1.0D, 0.0D, 0.0D);

    private BulletVfxRenderUtil() {
    }

    public static void sprite(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPosition,
                              Vec3 worldPosition, ResourceLocation texture, int color, float size) {
        Vec3 facing = cameraPosition.subtract(worldPosition);
        if (facing.lengthSqr() <= EPSILON) return;
        facing = facing.normalize();
        Vec3 up = Math.abs(facing.dot(UP)) > 0.98D ? RIGHT : UP;
        Vec3 right = facing.cross(up).normalize();
        up = right.cross(facing).normalize();

        float halfSize = size * 0.5F;
        Vec3 rightOffset = right.scale(halfSize);
        Vec3 upOffset = up.scale(halfSize);
        Vec3 relativeCenter = worldPosition.subtract(cameraPosition);
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer buffer = bufferSource.getBuffer(TGRenderTypes.trail(texture, true));
        texturedVertex(buffer, matrix, relativeCenter.subtract(rightOffset).subtract(upOffset), color, 0.0F, 1.0F);
        texturedVertex(buffer, matrix, relativeCenter.add(rightOffset).subtract(upOffset), color, 1.0F, 1.0F);
        texturedVertex(buffer, matrix, relativeCenter.add(rightOffset).add(upOffset), color, 1.0F, 0.0F);
        texturedVertex(buffer, matrix, relativeCenter.subtract(rightOffset).add(upOffset), color, 0.0F, 0.0F);
    }

    public static void rectangle(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPosition,
                                 Vec3 worldPosition, int color, float length, float width, float rotation) {
        Vec3 facing = cameraPosition.subtract(worldPosition);
        if (facing.lengthSqr() <= EPSILON) return;
        facing = facing.normalize();
        Vec3 up = Math.abs(facing.dot(UP)) > 0.98D ? RIGHT : UP;
        Vec3 right = facing.cross(up).normalize();
        up = right.cross(facing).normalize();

        float cosine = (float) Math.cos(rotation);
        float sine = (float) Math.sin(rotation);
        Vec3 lengthAxis = right.scale(cosine).add(up.scale(sine));
        Vec3 widthAxis = right.scale(-sine).add(up.scale(cosine));
        Vec3 lengthOffset = lengthAxis.scale(length * 0.5D);
        Vec3 widthOffset = widthAxis.scale(width * 0.5D);
        Vec3 relativeCenter = worldPosition.subtract(cameraPosition);
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer buffer = bufferSource.getBuffer(TGRenderTypes.confetti());
        colorVertex(buffer, matrix, relativeCenter.subtract(lengthOffset).subtract(widthOffset), color);
        colorVertex(buffer, matrix, relativeCenter.add(lengthOffset).subtract(widthOffset), color);
        colorVertex(buffer, matrix, relativeCenter.add(lengthOffset).add(widthOffset), color);
        colorVertex(buffer, matrix, relativeCenter.subtract(lengthOffset).add(widthOffset), color);
    }

    public static int fadeColor(int color, float fade) {
        int alpha = Math.round(FastColor.ARGB32.alpha(color) * fade);
        return FastColor.ARGB32.color(alpha,
                FastColor.ARGB32.red(color),
                FastColor.ARGB32.green(color),
                FastColor.ARGB32.blue(color));
    }

    private static void texturedVertex(VertexConsumer buffer, Matrix4f matrix, Vec3 position,
                                       int color, float u, float v) {
        buffer.addVertex(matrix, (float) position.x, (float) position.y, (float) position.z)
                .setColor(color)
                .setUv(u, v)
                .setLight(0xF000F0);
    }

    private static void colorVertex(VertexConsumer buffer, Matrix4f matrix, Vec3 position, int color) {
        buffer.addVertex(matrix, (float) position.x, (float) position.y, (float) position.z)
                .setColor(color);
    }
}
