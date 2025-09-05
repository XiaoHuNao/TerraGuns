package org.confluence.terra_guns.client.model.item;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class GunModel<T extends BaseGun> extends GeoModel<T> {
    private final ResourceLocation model;
    private final ResourceLocation texture;
    private final ResourceLocation animation;

    public GunModel(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;
    }

    public GunModel(ResourceLocation name) {
        this.model = name.withPath("geo/gun/%s.geo.json".formatted(name.getPath()));
        this.texture = name.withPath("textures/gun/%s.png".formatted(name.getPath()));
        this.animation = name.withPath("animations/gun/%s.animation.json".formatted(name.getPath()));
    }

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
    }
}
