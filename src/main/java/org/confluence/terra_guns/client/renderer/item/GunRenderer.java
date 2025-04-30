package org.confluence.terra_guns.client.renderer.item;

import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class GunRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
    public GunRenderer(GeoModel<T> model) {
        super(model);
//        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
