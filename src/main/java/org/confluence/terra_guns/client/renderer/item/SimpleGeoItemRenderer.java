package org.confluence.terra_guns.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.Optional;

public class SimpleGeoItemRenderer<T extends Item & GeoAnimatable> implements IClientItemExtensions {
    private final ResourceLocation model;
    private final ResourceLocation texture;
    private final ResourceLocation animation;
    private GeoItemRenderer<T> renderer;

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

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) {
            this.renderer = new GeoItemRenderer<>(new GeoModel<>() {
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
                    Optional<GeoBone> fire = Optional.ofNullable(getAnimationProcessor().getBone("Fire"));
                    Optional<GeoBone> fire1 = Optional.ofNullable(getAnimationProcessor().getBone("Fire1"));

                    AnimationController<T> controller = animationState.getController();
                    Optional<AnimationProcessor.QueuedAnimation> currentAnimation = Optional.ofNullable(controller.getCurrentAnimation());

                    if (currentAnimation.isPresent() && controller.getAnimationState() != AnimationController.State.STOPPED) {
                        if (!currentAnimation.get().animation().name().equals("fire")) return;
                        fire.ifPresent(geoBone -> geoBone.setHidden(false));
                        fire1.ifPresent(geoBone -> geoBone.setHidden(false));
                    } else {
                        fire.ifPresent(geoBone -> geoBone.setHidden(true));
                        fire1.ifPresent(geoBone -> geoBone.setHidden(true));
                    }
                }
            });
        }
        return renderer;
    }
}