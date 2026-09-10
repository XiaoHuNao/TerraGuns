package org.confluence.terra_guns.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Applies TACZ's animation constraint (ICA) to a GeckoLib Bedrock model.
 *
 * <p>The {@code constraint} bone is an empty marker placed at the iron-sight
 * point. Its animated position and rotation are not a visible transform; the
 * values are the three translation and rotation ICA coefficients. The marker
 * itself is therefore excluded from the animated path while its parents are
 * evaluated normally.</p>
 */
final class TaczAnimationConstraint {
    private static final float MODEL_UNIT = 1.0F / 16.0F;
    private static final float DEFAULT_ICA = 0.2F;
    private static final float EPSILON = 0.0001F;

    private TaczAnimationConstraint() {
    }

    static void apply(PoseStack poseStack, GeoBone constraint, float weight) {
        if (constraint == null || weight <= 0.0F) {
            return;
        }

        List<GeoBone> path = pathToRoot(constraint);
        if (path.isEmpty()) {
            return;
        }

        Matrix4f animatedPath = buildConstraintPath(path, false);
        Matrix4f originPath = buildConstraintPath(path, true);
        if (animatedPath == null || originPath == null) {
            return;
        }

        Vector3f animatedPoint = animatedPath.getTranslation(new Vector3f());
        Vector3f originPoint = originPath.getTranslation(new Vector3f());
        Vector3f ica = readIca(constraint);

        // Move the point back toward its unanimated position. ICA=0 means
        // fully locked, ICA=1 means the animation is left untouched.
        Vector3f translationCorrection = originPoint.sub(animatedPoint);
        translationCorrection.mul(
                (1.0F - ica.x()) * weight,
                (1.0F - ica.y()) * weight,
                (1.0F - ica.z()) * weight);

        if (translationCorrection.lengthSquared() > EPSILON * EPSILON) {
            poseStack.translate(translationCorrection.x(), translationCorrection.y(), translationCorrection.z());
        }

        // Build the rotation that changes the animated constraint orientation
        // into the origin orientation, then attenuate each Euler component by
        // the corresponding ICA coefficient. The order matches GeckoLib's
        // Bedrock bone order: Z, Y, X.
        Quaternionf animatedRotation = animatedPath.getNormalizedRotation(new Quaternionf());
        Quaternionf originRotation = originPath.getNormalizedRotation(new Quaternionf());
        Quaternionf correctionRotation = new Quaternionf(originRotation)
                .mul(new Quaternionf(animatedRotation).invert())
                .normalize();
        Vector3f correctionAngles = correctionRotation.getEulerAnglesXYZ(new Vector3f());

        float xRotation = correctionAngles.x() * (1.0F - ica.x()) * weight;
        float yRotation = correctionAngles.y() * (1.0F - ica.y()) * weight;
        float zRotation = correctionAngles.z() * (1.0F - ica.z()) * weight;
        if (Math.abs(xRotation) > EPSILON || Math.abs(yRotation) > EPSILON || Math.abs(zRotation) > EPSILON) {
            poseStack.translate(animatedPoint.x(), animatedPoint.y(), animatedPoint.z());
            poseStack.mulPose(Axis.ZP.rotation(zRotation));
            poseStack.mulPose(Axis.YP.rotation(yRotation));
            poseStack.mulPose(Axis.XP.rotation(xRotation));
            poseStack.translate(-animatedPoint.x(), -animatedPoint.y(), -animatedPoint.z());
        }
    }

    private static List<GeoBone> pathToRoot(GeoBone leaf) {
        List<GeoBone> path = new ArrayList<>();
        for (GeoBone bone = leaf; bone != null; bone = bone.getParent()) {
            path.add(bone);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Build the transform of the constraint point in GeckoLib's model space.
     *
     * <p>For every parent we must use the complete GeckoLib bone transform,
     * including the final translation away from its pivot.  The old version
     * left that translation out, which made a rotated {@code Root} rotate
     * around the wrong origin and produced a large, apparently random camera
     * displacement during inspect.</p>
     */
    private static Matrix4f buildConstraintPath(List<GeoBone> path, boolean origin) {
        PoseStack stack = new PoseStack();
        GeoBone constraint = path.get(path.size() - 1);

        for (int i = 0; i < path.size(); i++) {
            GeoBone bone = path.get(i);
            BoneSnapshot initial = bone.getInitialSnapshot();
            if (initial == null) {
                return null;
            }

            boolean isConstraint = bone == constraint;
            if (isConstraint) {
                // The values keyed on constraint are ICA coefficients, not a
                // real bone transform.  Only locate its pivot; its own
                // position/rotation must not move the point we are locking.
                stack.translate(bone.getPivotX() * MODEL_UNIT,
                        bone.getPivotY() * MODEL_UNIT,
                        bone.getPivotZ() * MODEL_UNIT);
                break;
            }

            boolean useInitial = origin;
            applyBoneTransform(stack, bone, initial, useInitial);
        }

        return new Matrix4f(stack.last().pose());
    }

    private static void applyBoneTransform(PoseStack stack, GeoBone bone,
                                            BoneSnapshot initial, boolean useInitial) {
        float posX = useInitial ? initial.getOffsetX() : bone.getPosX();
        float posY = useInitial ? initial.getOffsetY() : bone.getPosY();
        float posZ = useInitial ? initial.getOffsetZ() : bone.getPosZ();
        float rotX = useInitial ? initial.getRotX() : bone.getRotX();
        float rotY = useInitial ? initial.getRotY() : bone.getRotY();
        float rotZ = useInitial ? initial.getRotZ() : bone.getRotZ();
        float scaleX = useInitial ? initial.getScaleX() : bone.getScaleX();
        float scaleY = useInitial ? initial.getScaleY() : bone.getScaleY();
        float scaleZ = useInitial ? initial.getScaleZ() : bone.getScaleZ();

        // This is RenderUtil.prepMatrixForBone expressed locally.  Do not
        // replace the pivot with a parent-relative pivot: GeoBone pivots are
        // model-space coordinates, and GeckoLib removes the same absolute
        // pivot after rotating the bone.
        stack.translate(-posX * MODEL_UNIT, posY * MODEL_UNIT, posZ * MODEL_UNIT);
        stack.translate(bone.getPivotX() * MODEL_UNIT,
                bone.getPivotY() * MODEL_UNIT,
                bone.getPivotZ() * MODEL_UNIT);
        stack.mulPose(Axis.ZP.rotation(rotZ));
        stack.mulPose(Axis.YP.rotation(rotY));
        stack.mulPose(Axis.XP.rotation(rotX));
        stack.scale(scaleX, scaleY, scaleZ);
        stack.translate(-bone.getPivotX() * MODEL_UNIT,
                -bone.getPivotY() * MODEL_UNIT,
                -bone.getPivotZ() * MODEL_UNIT);
    }

    private static Vector3f readIca(GeoBone constraint) {
        BoneSnapshot initial = constraint.getInitialSnapshot();
        if (initial == null) {
            return new Vector3f(DEFAULT_ICA);
        }

        float positionX = constraint.getPosX() - initial.getOffsetX();
        float positionY = constraint.getPosY() - initial.getOffsetY();
        float positionZ = constraint.getPosZ() - initial.getOffsetZ();
        float rotationX = (float) Math.toDegrees(constraint.getRotX() - initial.getRotX());
        float rotationY = (float) Math.toDegrees(constraint.getRotY() - initial.getRotY());
        float rotationZ = (float) Math.toDegrees(constraint.getRotZ() - initial.getRotZ());

        return new Vector3f(
                readIcaValue(positionX, rotationX),
                readIcaValue(positionY, rotationY),
                readIcaValue(positionZ, rotationZ));
    }

    private static float readIcaValue(float positionValue, float rotationValue) {
        // Animations that do not key the optional constraint bone are still
        // valid gun animations. Keep TACZ's recommended default in that case.
        if (Float.isFinite(positionValue) && Math.abs(positionValue) >= EPSILON) {
            return clamp(positionValue);
        }
        if (Float.isFinite(rotationValue) && Math.abs(rotationValue) >= EPSILON) {
            return clamp(rotationValue);
        }
        return DEFAULT_ICA;
    }

    private static float clamp(float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }
}
