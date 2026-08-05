package org.confluence.terra_guns.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.confluence.terra_guns.api.client.animation.HandAnimationAction;
import org.confluence.terra_guns.client.animation.GunCameraAnimation;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;

import java.util.List;

public class SimpleGeoItemRenderer<T extends Item & GeoAnimatable> implements IClientItemExtensions {
    private final ResourceLocation model;
    private final ResourceLocation texture;
    private final ResourceLocation animation;
    private GunRenderer<T> renderer;

    public SimpleGeoItemRenderer(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;
    }

    public SimpleGeoItemRenderer(DefaultedItemGeoModel<T> gunItemModel) {
        this.model = gunItemModel.getModelResource(null);
        this.texture = gunItemModel.getTextureResource(null);
        this.animation = gunItemModel.getAnimationResource(null);
    }

    /**
     * Let the TACZ-compatible model own the complete first-person pose. When
     * this returns true, NeoForge skips the vanilla hand/item transform and
     * still invokes this extension's custom renderer for the actual model.
     */
    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm,
                                           ItemStack itemStack, float partialTick, float equippedProgress,
                                           float swingProgress) {
        // NeoForge calls this hook before the vanilla per-item hand transform.
        // Returning true transfers that entire transform to GunRenderer, so
        // applying its inverse here would add a second, unwanted hand pose.
        // ItemInHandRenderer has already applied view bob, however.  TACZ-
        // style first-person transforms are camera-relative, so remove only
        // that matrix before the custom renderer runs.
        removeVanillaViewBobbing(poseStack, player, partialTick);
        return true;
    }

    private static void removeVanillaViewBobbing(PoseStack poseStack, LocalPlayer player, float partialTick) {
        // Matches TACZ's renderFirstPerson implementation. ItemInHandRenderer
        // applies the view-bob rotations in this order before this hook, so
        // append the same two inverse rotations in the same order.
        float xBob = Mth.lerp(partialTick, player.xBobO, player.xBob);
        float yBob = Mth.lerp(partialTick, player.yBobO, player.yBob);
        float xRotation = player.getViewXRot(partialTick) - xBob;
        float yRotation = player.getViewYRot(partialTick) - yBob;
        poseStack.mulPose(Axis.XP.rotationDegrees(-xRotation * 0.1F));
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRotation * 0.1F));
    }

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) {
            this.renderer = new GunRenderer<>(new GeoModel<>() {
                @Override
                public ResourceLocation getModelResource(T animatable) {
                    return model;
                }

                @Override
                public ResourceLocation getTextureResource(T animatable) {
                    return texture;
                }

                @Override
                public ResourceLocation getAnimationResource(T animatable) {
                    return animation;
                }

                @Override
                public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
                    super.setCustomAnimations(animatable, instanceId, animationState);
                    boolean isFiring = this.isFiring(animatable, instanceId, animationState);

                    List<String> fireBones = List.of("Fire", "Fire1", "Fire2", "Fire3");
                    for (String boneName : fireBones) {
                        GeoBone bone = getAnimationProcessor().getBone(boneName);
                        if (bone != null) {
                            bone.setHidden(!isFiring);
                        }
                    }

                    // Shell bones are animated out of the weapon during the
                    // shot. Once GeckoLib finishes the triggered animation it
                    // restores unkeyed bones to their default transform; that
                    // would make the same shell visibly snap back into the
                    // chamber. Keep it hidden outside the firing clip so the
                    // reset pose can never render a second copy.
                    for (String boneName : List.of("Shell", "shell", "Shell1", "shell1")) {
                        GeoBone bone = getAnimationProcessor().getBone(boneName);
                        if (bone != null) {
                            bone.setHidden(!isFiring);
                        }
                    }

                    for (String boneName : List.of("lefthand_pos", "righthand_pos")) {
                        GeoBone bone = getAnimationProcessor().getBone(boneName);
                        if (bone != null) {
                            bone.setHidden(true);
                        }
                    }

                    if (isFirstPersonPerspective() && controlsCamera(animatable, animationState)) {
                        GunCameraAnimation.capture(getAnimationProcessor().getBone("camera"));
                    }

                }

                private boolean isFirstPersonPerspective() {
                    ItemDisplayContext perspective = SimpleGeoItemRenderer.this.renderer.getRenderPerspective();
                    return perspective == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                            || perspective == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
                }

                private boolean controlsCamera(T animatable, AnimationState<T> animationState) {
                    if (!(animatable instanceof BaseGun baseGun)) {
                        return false;
                    }

                    AnimationController<T> controller = animationState.getController();
                    if (controller.getAnimationState() == AnimationController.State.STOPPED) {
                        return false;
                    }
                    AnimationProcessor.QueuedAnimation currentAnimation = controller.getCurrentAnimation();
                    if (currentAnimation == null) {
                        return false;
                    }

                    String animationName = currentAnimation.animation().name();
                    return baseGun.getAnimationProfile().isAnimation(HandAnimationAction.DRAW, animationName)
                            || baseGun.getAnimationProfile().isAnimation(HandAnimationAction.PUT_AWAY, animationName)
                            || baseGun.getAnimationProfile().isAnimation(HandAnimationAction.INSPECT, animationName)
                            || baseGun.getAnimationProfile().isAnimation(HandAnimationAction.SHOOT, animationName);
                }

                private boolean isFiring(T animatable, long instanceId, AnimationState<T> animationState) {
                    if (animatable instanceof BaseGun baseGun) {
                        if (baseGun.isAnimationPlaying(instanceId, HandAnimationAction.SHOOT)) {
                            return true;
                        }
                    }

                    AnimationController<T> controller = animationState.getController();
                    AnimationController.State state = controller.getAnimationState();

                    if (state == AnimationController.State.STOPPED) {
                        return false;
                    }
                    AnimationProcessor.QueuedAnimation currentAnimation = controller.getCurrentAnimation();
                    if (currentAnimation == null) {
                        return false;
                    }
                    if (animatable instanceof BaseGun baseGun) {
                        return baseGun.getAnimationProfile()
                                .isAnimation(HandAnimationAction.SHOOT, currentAnimation.animation().name());
                    }
                    return "fire".equals(currentAnimation.animation().name())
                            || "shoot".equals(currentAnimation.animation().name());
                }
            });
        }
        return renderer;
    }
}
