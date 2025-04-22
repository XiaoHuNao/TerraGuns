package org.confluence.terra_guns.client.model.item;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import software.bernie.geckolib.model.GeoModel;

public class GunModel<T extends BaseGun> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }
}
