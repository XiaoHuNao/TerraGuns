package org.confluence.terra_guns.client.renderer.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.confluence.terra_guns.client.model.item.GunItemModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SimpleGeoItemRenderer<T extends Item & GeoAnimatable> implements IClientItemExtensions {
    private final ResourceLocation model;
    private final ResourceLocation texture;
    private final @Nullable ResourceLocation animation;
    private GeoItemRenderer<T> renderer;

    public SimpleGeoItemRenderer(ResourceLocation model, ResourceLocation texture, @Nullable ResourceLocation animation) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;
    }

    public SimpleGeoItemRenderer(GunItemModel gunItemModel) {
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
            });
        }
        return renderer;
    }
}
