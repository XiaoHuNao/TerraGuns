package org.confluence.terra_guns.common.item.gun;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.projectile.Projectile;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.entity.SimpleTrailProjectile;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class GeoGunItem<T extends Projectile> extends GunItem<T> implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

    public GeoGunItem(float baseDamage, float ammoSpeed, int useDelay, float knockBack, float crit, float inaccuracy) {
        super(baseDamage, ammoSpeed, useDelay, knockBack, crit, inaccuracy);
    }
    public GeoGunItem(float baseDamage, float ammoSpeed, int useDelay, float knockBack, float crit) {
        super(baseDamage, ammoSpeed, useDelay, knockBack, crit);
    }
    public GeoGunItem(Properties properties) {
        super(properties);
    }

    public GeoGunItem(Properties properties, float baseDamage, float ammoSpeed, int useDelay, float knockBack, float crit, float inaccuracy) {
        super(properties, baseDamage, ammoSpeed, useDelay, knockBack, crit, inaccuracy);
    }
    public GeoGunItem(Properties properties, float baseDamage, float ammoSpeed, int useDelay, float knockBack, float crit) {
        super(properties, baseDamage, ammoSpeed, useDelay, knockBack, crit);
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoItemRenderer<GeoGunItem<SimpleTrailProjectile>> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (renderer == null) {
                    String path = BuiltInRegistries.ITEM.getKey(GeoGunItem.this).getPath();
                    this.renderer = new GeoItemRenderer<>(new DefaultedItemGeoModel<>(TerraGuns.asResource("guns/" + path)));
                }
                return renderer;
            }
        });
    }
}
