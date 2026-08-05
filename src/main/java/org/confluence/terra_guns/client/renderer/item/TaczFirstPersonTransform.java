package org.confluence.terra_guns.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies TACZ locator inverses to a model after GeckoLib has baked the
 * Bedrock geometry into its own coordinate system.
 *
 * <p>GeckoLib mirrors Bedrock's X coordinates while retaining Y/Z in the
 * renderable Geo model. Therefore the Bedrock renderer's outer
 * {@code translate(0, 1.5, 0) + Z180} must not be applied a second time by a
 * GeoItemRenderer. This class works entirely in the baked Geo coordinates.</p>
 */
final class TaczFirstPersonTransform {
    private static final float MODEL_UNIT = 1.0F / 16.0F;

    private TaczFirstPersonTransform() {
    }

    static void applyIdleViewInverse(PoseStack poseStack, GeoBone idleView) {
        applyPositioningInverse(poseStack, idleView);
    }

    /**
     * Apply the inverse of a TACZ locator path in GeckoLib's baked coordinate
     * space. The same rule is used for first-person view locators and for the
     * ground/third-person/fixed display locators.
     */
    static void applyPositioningInverse(PoseStack poseStack, GeoBone locator) {
        if (locator == null) {
            return;
        }

        List<GeoBone> path = new ArrayList<>();
        for (GeoBone bone = locator; bone != null; bone = bone.getParent()) {
            path.add(bone);
        }

        // GeckoLib renders a bone as Z -> Y -> X. The inverse therefore
        // walks X -> Y -> Z, then removes the bone's baked Geo-space pivot.
        for (GeoBone bone : path) {
            poseStack.mulPose(Axis.XP.rotation(-bone.getRotX()));
            poseStack.mulPose(Axis.YP.rotation(-bone.getRotY()));
            poseStack.mulPose(Axis.ZN.rotation(bone.getRotZ()));

            GeoBone parent = bone.getParent();
            float partX = bone.getPivotX();
            float partY = bone.getPivotY();
            float partZ = bone.getPivotZ();
            if (parent != null) {
                partX -= parent.getPivotX();
                partY -= parent.getPivotY();
                partZ -= parent.getPivotZ();
            }

            poseStack.translate(-partX * MODEL_UNIT,
                    -partY * MODEL_UNIT,
                    -partZ * MODEL_UNIT);
        }
    }
}
