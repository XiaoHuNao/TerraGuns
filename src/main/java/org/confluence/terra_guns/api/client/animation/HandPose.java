package org.confluence.terra_guns.api.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

/**
 * A first-person hand transform. Translation is in model units, rotation is in degrees,
 * and scale is relative to the normal held-item pose.
 */
public record HandPose(Vec3 translation, Vec3 rotation, Vec3 scale) {
    public static final HandPose IDENTITY = new HandPose(Vec3.ZERO, Vec3.ZERO, new Vec3(1.0D, 1.0D, 1.0D));

    public HandPose {
        Objects.requireNonNull(translation, "translation");
        Objects.requireNonNull(rotation, "rotation");
        Objects.requireNonNull(scale, "scale");
        checkFinite(translation, "translation");
        checkFinite(rotation, "rotation");
        checkFinite(scale, "scale");
        if (scale.x() < 0.0D || scale.y() < 0.0D || scale.z() < 0.0D) {
            throw new IllegalArgumentException("Hand-pose scale cannot be negative");
        }
    }

    public static HandPose of(double x, double y, double z, double pitch, double yaw, double roll) {
        return new HandPose(new Vec3(x, y, z), new Vec3(pitch, yaw, roll), new Vec3(1.0D, 1.0D, 1.0D));
    }

    public static HandPose lerp(HandPose from, HandPose to, float progress) {
        double t = clamp(progress, 0.0F, 1.0F);
        return new HandPose(
                lerp(from.translation, to.translation, t),
                lerp(from.rotation, to.rotation, t),
                lerp(from.scale, to.scale, t)
        );
    }

    /**
     * Applies the pose and mirrors the horizontal components for the left hand.
     * This lets one animation definition work for both hands.
     */
    public void apply(PoseStack poseStack, HumanoidArm arm) {
        float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack.translate(translation.x() * side, translation.y(), translation.z());
        poseStack.mulPose(Axis.XP.rotationDegrees((float) rotation.x()));
        poseStack.mulPose(Axis.YP.rotationDegrees((float) rotation.y() * side));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) rotation.z() * side));
        poseStack.scale((float) scale.x(), (float) scale.y(), (float) scale.z());
    }

    private static Vec3 lerp(Vec3 from, Vec3 to, double progress) {
        return new Vec3(
                from.x() + (to.x() - from.x()) * progress,
                from.y() + (to.y() - from.y()) * progress,
                from.z() + (to.z() - from.z()) * progress
        );
    }

    private static void checkFinite(Vec3 vector, String name) {
        if (!Double.isFinite(vector.x()) || !Double.isFinite(vector.y()) || !Double.isFinite(vector.z())) {
            throw new IllegalArgumentException(name + " must contain finite values");
        }
    }

    private static double clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
