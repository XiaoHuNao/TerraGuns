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
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

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

        if (applyFirstPersonView) {
            poseStack.pushPose();

            // ItemRenderer has already translated the custom-renderer stack
            // by (-0.5, -0.5, -0.5), and GeoItemRenderer adds
            // (+0.5, +0.51, +0.5) in preRender. Their net result is only a
            // +0.01 Y residual. Do not subtract another half-block here:
            // that would move the whole gun to the left and into the camera.
            poseStack.translate(0.0F, -0.01F, 0.0F);

            // This translation is deliberately before the view-node inverse,
            // so it remains camera-relative: +X is screen-right, -Y is
            // screen-down, and -Z moves the model away from the camera.
            float side = renderPerspective == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ? -1.0F : 1.0F;
            poseStack.translate(side * FIRST_PERSON_X, FIRST_PERSON_Y, FIRST_PERSON_Z);

            // GeckoLib's Bedrock loader has already baked the TACZ model
            // axes into GeoBone/GeoCube coordinates. Applying TACZ's
            // render-origin Z180 here a second time flips the pistol over.
            GeoBone idleView = getGeoModel().getAnimationProcessor().getBone("idle_view");
            if (idleView == null) {
                idleView = getGeoModel().getBone("idle_view").orElse(null);
            }
            TaczFirstPersonTransform.applyIdleViewInverse(poseStack, idleView);

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
    public void renderFinal(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        // Render the arms only after every top-level gun bone has finished.
        // This prevents the gun's later sibling bones from depth-occluding
        // the hand and also gives us a stable point at which to recover from
        // a marker callback that was skipped by the model traversal.
        if (isFirstPersonPerspective() && firstPersonBasePose != null) {
            queueMissingModelHands();
            renderQueuedArm(bufferSource, firstPersonArmLight, queuedLeftArmPose, queuedLeftArmNormal, HumanoidArm.LEFT);
            renderQueuedArm(bufferSource, firstPersonArmLight, queuedRightArmPose, queuedRightArmNormal, HumanoidArm.RIGHT);
        }

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

    private void queuePlayerArm(PoseStack poseStack, GeoBone handPosition, HumanoidArm arm) {
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

    /**
     * The arm marker is intentionally hidden and some GeckoLib model paths do
     * not invoke this renderer override for hidden marker bones. Rebuild the
     * marker transform from the animated bone hierarchy as a fallback instead
     * of leaving the arm pose/normal null.
     */
    private void queueMissingModelHands() {
        if (queuedLeftArmPose == null || queuedLeftArmNormal == null) {
            GeoBone marker = findModelBone("lefthand_pos");
            if (marker != null) {
                queuePlayerArmFromModel(marker, HumanoidArm.LEFT);
            }
        }
        if (queuedRightArmPose == null || queuedRightArmNormal == null) {
            GeoBone marker = findModelBone("righthand_pos");
            if (marker != null) {
                queuePlayerArmFromModel(marker, HumanoidArm.RIGHT);
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

        Matrix4f visiblePose = new Matrix4f()
                .translation(0.0F, 0.0F, -2F)
                .mul(pose);
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
}
