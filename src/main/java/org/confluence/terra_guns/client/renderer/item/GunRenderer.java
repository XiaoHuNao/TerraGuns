package org.confluence.terra_guns.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.confluence.terra_guns.client.init.TGKeys;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GunRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
    // The TACZ positioning nodes define the camera pose, while this small
    // camera-space baseline is the equivalent of the usual first-person item
    // placement. It keeps a GeoItemRenderer model away from the near clip
    // plane and places it in the lower-right hand area after the custom hook
    // has skipped vanilla's per-item transform.
    private static final float FIRST_PERSON_X = 0.56F;
    private static final float FIRST_PERSON_Y = -0.20F;
    private static final float FIRST_PERSON_Z = -0.72F;

    // Arms are queued while the model is traversed and rendered after the
    // complete gun. Rendering them at the marker immediately would let later
    // gun cubes (the pistol/slide siblings) depth-occlude the hand entirely.
    private Matrix4f queuedLeftArmPose;
    private Matrix3f queuedLeftArmNormal;
    private Matrix4f queuedRightArmPose;
    private Matrix3f queuedRightArmNormal;
    // Hidden marker bones are not traversed by every GeckoLib render path.
    // Keep the last valid marker transform as a local pose so the one-tick
    // reset between a play-once animation and its idle animation cannot make
    // the real player arms jump to the model's raw/default bone pose.
    private Matrix4f lastLeftArmLocalPose;
    private Matrix3f lastLeftArmLocalNormal;
    private Matrix4f lastRightArmLocalPose;
    private Matrix3f lastRightArmLocalNormal;
    private ItemDisplayContext lastRenderedPerspective;
    // GeckoLib can reset every animated bone to its initial snapshot for one
    // render before the channel's looped idle animation is installed. Keep a
    // complete bone-value snapshot as well as the hand matrices: otherwise
    // the gun root or one of its visible parent bones can still jump while
    // the separately rendered arms stay in place.
    private final Map<String, BonePose> lastModelPose = new HashMap<>();
    private final Map<String, BonePose> modelPoseBeforeOverride = new HashMap<>();
    private boolean modelPosePrepared;
    private boolean modelPoseOverridden;
    private Matrix4f firstPersonBasePose;
    private Matrix3f firstPersonBaseNormal;
    private int firstPersonArmLight;

    public GunRenderer(GeoModel<T> model) {
        super(model);
//        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    public ItemDisplayContext getRenderPerspective() {
        return renderPerspective;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
                                  MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                  float partialTick, int packedLight, int packedOverlay, int packedColor) {
        boolean applyFirstPersonView = isFirstPersonPerspective() && bone.getParent() == null;
        GeoBone displayPosition = !isFirstPersonPerspective() && bone.getParent() == null
                ? findDisplayPositionBone()
                : null;
        boolean applyDisplayPosition = displayPosition != null;

        if (bone.getParent() == null) {
            if (lastRenderedPerspective != null && lastRenderedPerspective != renderPerspective) {
                clearLastArmPoses();
            }
            lastRenderedPerspective = renderPerspective;
        }

        if (applyFirstPersonView) {
            prepareModelPoseForFrame();
            poseStack.pushPose();

            // ItemRenderer always centres a custom item renderer with
            // (-0.5, -0.5, -0.5), even when applyForgeHandTransform returns
            // true.  GeoItemRenderer.preRender then adds (+0.5, +0.51,
            // +0.5).  Their net result is only the known +0.01 Y residual;
            // do not subtract another half-block here or the gun will be
            // pushed into the upper-left corner and appear at the wrong
            // camera depth.
            poseStack.translate(0.0F, -0.01F, 0.0F);

            // This translation is deliberately before the view-node inverse,
            // so it remains camera-relative: +X is screen-right, -Y is
            // screen-down, and -Z moves the model away from the camera.
            float side = renderPerspective == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ? -1.0F : 1.0F;
            poseStack.translate(side * FIRST_PERSON_X, FIRST_PERSON_Y, FIRST_PERSON_Z);

            // TACZ's constraint point is evaluated after GeckoLib has ticked
            // the animation but before the root bone is rendered. Applying it
            // here keeps both the gun and the hand markers on the same
            // corrected transform.
            // The project does not have an interpolated ADS progress yet.
            // TACZ applies ICA with that progress, not as a permanent
            // correction.  Applying it at weight 1 during inspect makes a
            // rotating Root move the whole weapon around the sight point,
            // which is the source of the huge upper-left jump.
            float constraintWeight = TGKeys.AIM.get().isDown() ? 1.0F : 0.0F;
            TaczAnimationConstraint.apply(poseStack, findModelBone("constraint"), constraintWeight);

            // GeckoLib's Bedrock loader has already baked the TACZ model
            // axes into GeoBone/GeoCube coordinates. Applying TACZ's
            // render-origin Z180 here a second time flips the pistol over.
            GeoBone firstPersonLocator = findModelBone("idle_view");
            if (firstPersonLocator == null) {
                // The replacement handgun geo keeps the old idle_view pivot
                // on the camera bone. Its rotation is animated for view kick,
                // so apply only the static locator transform here; otherwise
                // the camera animation is inverted into the gun a second time.
                firstPersonLocator = findModelBone("camera");
            }
            TaczFirstPersonTransform.applyIdleViewInverse(poseStack, firstPersonLocator);

            // Keep the camera-space stack before GeckoLib applies the current
            // top-level bone. The model may have several top-level bones, so
            // this must be captured once and kept until doPostRenderCleanup.
            if (firstPersonBasePose == null) {
                firstPersonBasePose = new Matrix4f(poseStack.last().pose());
                firstPersonBaseNormal = new Matrix3f(poseStack.last().normal());
                firstPersonArmLight = packedLight;
            }
        } else if (applyDisplayPosition) {
            // TACZ-compatible models keep their non-first-person item
            // transforms in locator bones instead of Minecraft's JSON item
            // transform. Apply the inverse locator before rendering each
            // top-level geometry branch; the locator bones themselves contain
            // no visible cubes.
            poseStack.pushPose();
            TaczFirstPersonTransform.applyPositioningInverse(poseStack, displayPosition);
        }

        if (isFirstPersonPerspective()) {
            HumanoidArm arm = switch (bone.getName()) {
                case "lefthand_pos" -> HumanoidArm.LEFT;
                case "righthand_pos" -> HumanoidArm.RIGHT;
                default -> null;
            };
            if (arm != null) {
                queuePlayerArm(poseStack, bone, arm);
            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, packedColor);

        if (applyFirstPersonView) {
            poseStack.popPose();
        } else if (applyDisplayPosition) {
            poseStack.popPose();
        }
    }

    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType,
                               MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                               float partialTick, int packedLight, int packedOverlay, int packedColor) {
        // GeoModel.handleAnimations can skip setCustomAnimations when the
        // same item instance was already rendered at this tick. A GUI,
        // dropped-item, or third-person render can therefore arrive here with
        // the first-person hand animation still applied to the shared model.
        // Non-first-person renders must not run GeckoLib's animation update.
        // The same GeoModel is shared by every display context; allowing the
        // hand animation to update it here makes the resulting pose leak into
        // GUI, ground, and third-person renders.  In GeckoLib's renderer,
        // isReRender=true is the render-only path and skips handleAnimations.
        if (!isFirstPersonPerspective()) {
            resetModelForDisplayContext();
            super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer,
                    true, partialTick, packedLight, packedOverlay, packedColor);
            return;
        }

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, packedColor);
    }

    @Override
    public void renderFinal(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        // Render the arms only after every top-level gun bone has finished.
        // This prevents the gun's later sibling bones from depth-occluding
        // the hand and also gives us a stable point at which to recover from
        // a marker callback that was skipped by the model traversal.
        if (isFirstPersonPerspective() && firstPersonBasePose != null) {
            queueMissingModelHands();
            rememberArmPose(HumanoidArm.LEFT, queuedLeftArmPose, queuedLeftArmNormal);
            rememberArmPose(HumanoidArm.RIGHT, queuedRightArmPose, queuedRightArmNormal);
            renderQueuedArm(bufferSource, firstPersonArmLight, queuedLeftArmPose, queuedLeftArmNormal, HumanoidArm.LEFT);
            renderQueuedArm(bufferSource, firstPersonArmLight, queuedRightArmPose, queuedRightArmNormal, HumanoidArm.RIGHT);
        }

        finishModelPoseFrame();

        clearQueuedArms();
        firstPersonBasePose = null;
        firstPersonBaseNormal = null;
    }

    private boolean isFirstPersonPerspective() {
        return renderPerspective == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || renderPerspective == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
    }

    private GeoBone findDisplayPositionBone() {
        String boneName = switch (renderPerspective) {
            case GROUND -> "ground";
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> "thirdperson_hand";
            case FIXED, GUI -> "fixed";
            default -> null;
        };
        if (boneName == null) {
            return null;
        }

        GeoBone bone = getGeoModel().getAnimationProcessor().getBone(boneName);
        return bone != null ? bone : getGeoModel().getBone(boneName).orElse(null);
    }

    private void resetModelForDisplayContext() {
        for (GeoBone bone : getGeoModel().getAnimationProcessor().getRegisteredBones()) {
            BoneSnapshot snapshot = bone.getInitialSnapshot();
            if (snapshot == null) {
                continue;
            }

            bone.setPosX(snapshot.getOffsetX());
            bone.setPosY(snapshot.getOffsetY());
            bone.setPosZ(snapshot.getOffsetZ());
            bone.setRotX(snapshot.getRotX());
            bone.setRotY(snapshot.getRotY());
            bone.setRotZ(snapshot.getRotZ());
            bone.setScaleX(snapshot.getScaleX());
            bone.setScaleY(snapshot.getScaleY());
            bone.setScaleZ(snapshot.getScaleZ());
            bone.resetStateChanges();
        }

        // These bones are only meaningful in first-person rendering. Reset
        // their visibility too, because GeckoLib may skip the animation hook
        // that normally applies these hidden flags for display contexts.
        for (String boneName : List.of(
                "Fire", "Fire1", "Fire2", "Fire3",
                "Shell", "shell", "Shell1", "shell1",
                "lefthand_pos", "righthand_pos", "constraint")) {
            GeoBone bone = findModelBone(boneName);
            if (bone != null) {
                bone.setHidden(true);
            }
        }
    }

    private void queuePlayerArm(PoseStack poseStack, GeoBone handPosition, HumanoidArm arm) {
        // Hidden bones still enter GeckoLib's recursion. When a play-once
        // controller finishes, the hand parent can be temporarily restored to
        // its initial snapshot before the idle clip is installed. That
        // produces a perfectly valid-looking matrix, so checking for null in
        // renderFinal cannot catch it. Keep the previous pose for this exact
        // reset state and let the current camera base be rebuilt below.
        if (isInitialHandPose(handPosition) && hasLastArmPose(arm)) {
            reuseLastArmPose(arm);
            return;
        }

        // This callback runs while GeckoLib is walking the marker's parents.
        // Copy that live pose instead of rebuilding the hierarchy from scratch:
        // it already contains the current root/hand animation, view transform,
        // and the exact same pivot convention used to render the gun.
        PoseStack handPose = new PoseStack();
        handPose.last().pose().set(poseStack.last().pose());
        handPose.last().normal().set(poseStack.last().normal());

        // The marker itself is not part of the live recursion stack yet. Move
        // to its animated position and pivot, then keep the transform there as
        // the origin of the vanilla first-person arm.
        RenderUtil.translateMatrixToBone(handPose, handPosition);
        RenderUtil.translateAndRotateMatrixForBone(handPose, handPosition);
        RenderUtil.scaleMatrixForBone(handPose, handPosition);

        if (arm == HumanoidArm.RIGHT) {
            queuedRightArmPose = new Matrix4f(handPose.last().pose());
            queuedRightArmNormal = new Matrix3f(handPose.last().normal());
        } else {
            queuedLeftArmPose = new Matrix4f(handPose.last().pose());
            queuedLeftArmNormal = new Matrix3f(handPose.last().normal());
        }
    }

    private void prepareModelPoseForFrame() {
        if (modelPosePrepared) {
            return;
        }
        modelPosePrepared = true;
        modelPoseOverridden = false;

        if (!isTransientModelResetPose() || lastModelPose.isEmpty()) {
            return;
        }

        modelPoseBeforeOverride.clear();
        for (GeoBone bone : getGeoModel().getAnimationProcessor().getRegisteredBones()) {
            BonePose cachedPose = lastModelPose.get(bone.getName());
            if (cachedPose == null) {
                continue;
            }

            modelPoseBeforeOverride.put(bone.getName(), BonePose.capture(bone));
            cachedPose.applyTo(bone);
        }
        modelPoseOverridden = !modelPoseBeforeOverride.isEmpty();
    }

    private boolean isTransientModelResetPose() {
        boolean foundMarker = false;
        boolean allHandsAreInitial = true;
        for (String markerName : List.of("lefthand_pos", "righthand_pos")) {
            GeoBone marker = findModelBone(markerName);
            if (marker == null) {
                continue;
            }

            foundMarker = true;
            allHandsAreInitial &= isInitialHandPose(marker);
        }
        return foundMarker && allHandsAreInitial;
    }

    private void finishModelPoseFrame() {
        if (!modelPosePrepared) {
            return;
        }

        if (modelPoseOverridden) {
            for (GeoBone bone : getGeoModel().getAnimationProcessor().getRegisteredBones()) {
                BonePose originalPose = modelPoseBeforeOverride.get(bone.getName());
                if (originalPose != null) {
                    originalPose.applyTo(bone);
                }
                // Applying the render-only pose marks bones as changed. Clear
                // those markers so the next GeckoLib tick sees the same state
                // it would have seen without this render guard.
                bone.resetStateChanges();
            }
            modelPoseBeforeOverride.clear();
        } else if (isFirstPersonPerspective()) {
            lastModelPose.clear();
            for (GeoBone bone : getGeoModel().getAnimationProcessor().getRegisteredBones()) {
                lastModelPose.put(bone.getName(), BonePose.capture(bone));
            }
        }

        modelPosePrepared = false;
        modelPoseOverridden = false;
    }

    private boolean isInitialHandPose(GeoBone marker) {
        GeoBone hand = marker.getParent();
        BoneSnapshot initial = hand == null ? null : hand.getInitialSnapshot();
        if (initial == null) {
            return false;
        }

        return isClose(hand.getPosX(), initial.getOffsetX())
                && isClose(hand.getPosY(), initial.getOffsetY())
                && isClose(hand.getPosZ(), initial.getOffsetZ())
                && isClose(hand.getRotX(), initial.getRotX())
                && isClose(hand.getRotY(), initial.getRotY())
                && isClose(hand.getRotZ(), initial.getRotZ())
                && isClose(hand.getScaleX(), initial.getScaleX())
                && isClose(hand.getScaleY(), initial.getScaleY())
                && isClose(hand.getScaleZ(), initial.getScaleZ());
    }

    private boolean hasLastArmPose(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT
                ? lastRightArmLocalPose != null && lastRightArmLocalNormal != null
                : lastLeftArmLocalPose != null && lastLeftArmLocalNormal != null;
    }

    private static boolean isClose(float actual, float expected) {
        return Math.abs(actual - expected) < 0.0001F;
    }

    /**
     * The arm marker is intentionally hidden and some GeckoLib model paths do
     * not invoke this renderer override for hidden marker bones. Rebuild the
     * marker transform from the animated bone hierarchy as a fallback instead
     * of leaving the arm pose/normal null.
     */
    private void queueMissingModelHands() {
        if (queuedLeftArmPose == null || queuedLeftArmNormal == null) {
            if (lastLeftArmLocalPose != null && lastLeftArmLocalNormal != null) {
                reuseLastArmPose(HumanoidArm.LEFT);
            } else {
                GeoBone marker = findModelBone("lefthand_pos");
                if (marker != null) {
                    queuePlayerArmFromModel(marker, HumanoidArm.LEFT);
                }
            }
        }
        if (queuedRightArmPose == null || queuedRightArmNormal == null) {
            if (lastRightArmLocalPose != null && lastRightArmLocalNormal != null) {
                reuseLastArmPose(HumanoidArm.RIGHT);
            } else {
                GeoBone marker = findModelBone("righthand_pos");
                if (marker != null) {
                    queuePlayerArmFromModel(marker, HumanoidArm.RIGHT);
                }
            }
        }
    }

    private GeoBone findModelBone(String name) {
        GeoBone bone = getGeoModel().getAnimationProcessor().getBone(name);
        return bone != null ? bone : getGeoModel().getBone(name).orElse(null);
    }

    private void queuePlayerArmFromModel(GeoBone marker, HumanoidArm arm) {
        PoseStack handPose = new PoseStack();
        handPose.last().pose().set(firstPersonBasePose);
        handPose.last().normal().set(firstPersonBaseNormal);

        // Match GeoRenderer's normal recursion: parent bones use the complete
        // prep transform, while the marker itself stops before its children
        // and therefore uses only translate/rotate/scale.
        List<GeoBone> parents = new ArrayList<>();
        for (GeoBone parent = marker.getParent(); parent != null; parent = parent.getParent()) {
            parents.add(parent);
        }
        for (int i = parents.size() - 1; i >= 0; i--) {
            RenderUtil.prepMatrixForBone(handPose, parents.get(i));
        }
        RenderUtil.translateMatrixToBone(handPose, marker);
        RenderUtil.translateAndRotateMatrixForBone(handPose, marker);
        RenderUtil.scaleMatrixForBone(handPose, marker);

        if (arm == HumanoidArm.RIGHT) {
            queuedRightArmPose = new Matrix4f(handPose.last().pose());
            queuedRightArmNormal = new Matrix3f(handPose.last().normal());
        } else {
            queuedLeftArmPose = new Matrix4f(handPose.last().pose());
            queuedLeftArmNormal = new Matrix3f(handPose.last().normal());
        }
    }

    private void reuseLastArmPose(HumanoidArm arm) {
        if (firstPersonBasePose == null || firstPersonBaseNormal == null) {
            return;
        }

        if (arm == HumanoidArm.RIGHT) {
            queuedRightArmPose = new Matrix4f(firstPersonBasePose).mul(lastRightArmLocalPose);
            queuedRightArmNormal = new Matrix3f(firstPersonBaseNormal).mul(lastRightArmLocalNormal);
        } else {
            queuedLeftArmPose = new Matrix4f(firstPersonBasePose).mul(lastLeftArmLocalPose);
            queuedLeftArmNormal = new Matrix3f(firstPersonBaseNormal).mul(lastLeftArmLocalNormal);
        }
    }

    private void rememberArmPose(HumanoidArm arm, Matrix4f pose, Matrix3f normal) {
        if (pose == null || normal == null || firstPersonBasePose == null || firstPersonBaseNormal == null) {
            return;
        }

        Matrix4f basePoseInverse = new Matrix4f(firstPersonBasePose).invert();
        Matrix3f baseNormalInverse = new Matrix3f(firstPersonBaseNormal).invert();
        if (arm == HumanoidArm.RIGHT) {
            lastRightArmLocalPose = basePoseInverse.mul(new Matrix4f(pose));
            lastRightArmLocalNormal = baseNormalInverse.mul(new Matrix3f(normal));
        } else {
            lastLeftArmLocalPose = basePoseInverse.mul(new Matrix4f(pose));
            lastLeftArmLocalNormal = baseNormalInverse.mul(new Matrix3f(normal));
        }
    }

    private void renderQueuedArm(MultiBufferSource bufferSource, int packedLight, Matrix4f pose, Matrix3f normal, HumanoidArm arm) {
        if (pose == null || normal == null) {
            return;
        }

        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        if (!(Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)
                instanceof PlayerRenderer playerRenderer)) {
            return;
        }

        PoseStack handPose = new PoseStack();
        handPose.last().pose().set(pose);
        handPose.last().normal().set(normal);
        // Use the game's shared first-person buffer. The buffer passed through
        // a custom item renderer can be wrapped by another render layer and
        // may not be flushed together with the player's skin render type.
        renderPlayerArmDirect(playerRenderer, player, handPose, bufferSource, packedLight, arm);
    }

    /**
     * Render only the requested first-person arm without going through the
     * RenderArmEvent hook again. This renderer is already inside the custom
     * item transform, so another first-person arm hook can cancel or replace
     * the arm before it reaches the buffer.
     */
    @SuppressWarnings("unchecked")
    private static void renderPlayerArmDirect(PlayerRenderer playerRenderer, AbstractClientPlayer player,
                                              PoseStack poseStack, MultiBufferSource bufferSource,
                                              int packedLight, HumanoidArm arm) {
        PlayerModel<AbstractClientPlayer> playerModel =
                playerRenderer.getModel();
        playerModel.setAllVisible(false);
        playerModel.attackTime = 0.0F;
        playerModel.crouching = false;
        playerModel.swimAmount = 0.0F;
        playerModel.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        ModelPart armPart;
        ModelPart sleevePart;
        if (arm == HumanoidArm.RIGHT) {
            armPart = playerModel.rightArm;
            sleevePart = playerModel.rightSleeve;
        } else {
            armPart = playerModel.leftArm;
            sleevePart = playerModel.leftSleeve;
        }
        armPart.visible = true;
        sleevePart.visible = true;
        armPart.xRot = 0.0F;
        sleevePart.xRot = 0.0F;

        ResourceLocation skinTexture = player.getSkin().texture();
        armPart.render(poseStack, bufferSource.getBuffer(RenderType.entitySolid(skinTexture)),
                packedLight, OverlayTexture.NO_OVERLAY);
        sleevePart.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(skinTexture)),
                packedLight, OverlayTexture.NO_OVERLAY);
    }

    private void clearQueuedArms() {
        queuedLeftArmPose = null;
        queuedLeftArmNormal = null;
        queuedRightArmPose = null;
        queuedRightArmNormal = null;
    }

    private void clearLastArmPoses() {
        lastLeftArmLocalPose = null;
        lastLeftArmLocalNormal = null;
        lastRightArmLocalPose = null;
        lastRightArmLocalNormal = null;
        // The hotbar/GUI is rendered between two first-person frames and
        // deliberately resets the shared GeoModel to its static display
        // pose.  Do not discard the first-person model snapshot here: that
        // snapshot is exactly what protects the next frame from GeckoLib's
        // one-tick reset while a play-once animation hands off to idle.
    }

    private record BonePose(float posX, float posY, float posZ,
                            float rotX, float rotY, float rotZ,
                            float scaleX, float scaleY, float scaleZ) {
        private static BonePose capture(GeoBone bone) {
            return new BonePose(bone.getPosX(), bone.getPosY(), bone.getPosZ(),
                    bone.getRotX(), bone.getRotY(), bone.getRotZ(),
                    bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
        }

        private void applyTo(GeoBone bone) {
            bone.setPosX(posX);
            bone.setPosY(posY);
            bone.setPosZ(posZ);
            bone.setRotX(rotX);
            bone.setRotY(rotY);
            bone.setRotZ(rotZ);
            bone.setScaleX(scaleX);
            bone.setScaleY(scaleY);
            bone.setScaleZ(scaleZ);
        }
    }
}
