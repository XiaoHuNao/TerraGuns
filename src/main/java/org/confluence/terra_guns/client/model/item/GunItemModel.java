package org.confluence.terra_guns.client.model.item;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.gun.CustomGunItem;
import software.bernie.geckolib.model.GeoModel;

public class GunItemModel extends GeoModel<CustomGunItem> {
    public static ResourceLocation MODEL = null;
    public static ResourceLocation TEXTURE = null;
    public static ResourceLocation ANIMATOR = null;

    public GunItemModel(String name, boolean noAnimator){
        MODEL = TerraGuns.asResource("geo/item/guns/%s.geo.json".formatted(name));
        TEXTURE = TerraGuns.asResource("textures/item/guns/%s.png".formatted(name));
    }

    public GunItemModel(String name){
        MODEL = TerraGuns.asResource("geo/item/guns/%s.geo.json".formatted(name));
        TEXTURE = TerraGuns.asResource("textures/item/guns/%s.png".formatted(name));
        ANIMATOR = TerraGuns.asResource("animations/item/guns/%s.png".formatted(name));
    }

    @Override
    public ResourceLocation getModelResource(CustomGunItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(CustomGunItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CustomGunItem animatable) {
        return ANIMATOR;
    }
}
