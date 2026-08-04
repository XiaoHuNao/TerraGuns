package org.confluence.terra_guns.client.renderer.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class PartyConfettiEffect implements ActiveBulletVfx {
    private static final int[] COLORS = {
            FastColor.ARGB32.color(235, 255, 75, 180),
            FastColor.ARGB32.color(235, 255, 224, 66),
            FastColor.ARGB32.color(235, 68, 215, 255),
            FastColor.ARGB32.color(235, 105, 125, 255),
            FastColor.ARGB32.color(235, 88, 220, 111),
            FastColor.ARGB32.color(235, 255, 139, 61)
    };
    private static final int COUNT = 56;
    private final List<Piece> pieces;

    public PartyConfettiEffect(Vec3 position) {
        double phase = (position.x * 0.37D + position.y * 0.13D + position.z * 0.71D) * 0.25D;
        this.pieces = new ArrayList<>(COUNT);
        for (int index = 0; index < COUNT; index++) {
            double angle = Math.PI * 2.0D * index / COUNT + phase;
            double horizontalSpeed = 0.14D + (index % 5) * 0.035D;
            double verticalSpeed = 0.10D + ((index * 7) % 5) * 0.035D;
            Vec3 velocity = new Vec3(
                    Math.cos(angle) * horizontalSpeed,
                    verticalSpeed,
                    Math.sin(angle) * horizontalSpeed
            );
            float length = 0.075F + (index % 9) * 0.012F;
            float width = 0.012F + (index % 4) * 0.004F;
            float rotation = (float) (angle * 1.7D + index * 0.43D);
            float rotationSpeed = (index % 2 == 0 ? 1.0F : -1.0F) * (0.08F + (index % 4) * 0.025F);
            this.pieces.add(new Piece(
                    position.add(velocity.scale(0.15D)),
                    velocity,
                    COLORS[index % COLORS.length],
                    length,
                    width,
                    rotation,
                    rotationSpeed,
                    120 + index % 25
            ));
        }
    }

    @Override
    public boolean tick(ClientLevel level) {
        for (int index = pieces.size() - 1; index >= 0; index--) {
            if (!pieces.get(index).tick()) {
                pieces.remove(index);
            }
        }
        return !pieces.isEmpty();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPosition) {
        for (Piece piece : pieces) {
            piece.render(poseStack, bufferSource, cameraPosition);
        }
    }

    private static final class Piece {
        private Vec3 position;
        private Vec3 velocity;
        private final int color;
        private final float length;
        private final float width;
        private float rotation;
        private final float rotationSpeed;
        private int age;
        private final int lifetime;

        private Piece(Vec3 position, Vec3 velocity, int color, float length, float width,
                      float rotation, float rotationSpeed, int lifetime) {
            this.position = position;
            this.velocity = velocity;
            this.color = color;
            this.length = length;
            this.width = width;
            this.rotation = rotation;
            this.rotationSpeed = rotationSpeed;
            this.lifetime = lifetime;
        }

        private boolean tick() {
            position = position.add(velocity);
            velocity = velocity.add(0.0D, -0.007D, 0.0D).scale(0.985D);
            rotation += rotationSpeed;
            age++;
            return age < lifetime;
        }

        private void render(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPosition) {
            float fade = 1.0F - age / (float) lifetime;
            BulletVfxRenderUtil.rectangle(poseStack, bufferSource, cameraPosition, position,
                    BulletVfxRenderUtil.fadeColor(color, fade), length, width, rotation);
        }
    }
}
