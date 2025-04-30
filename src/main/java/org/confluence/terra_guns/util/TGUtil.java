package org.confluence.terra_guns.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.renderer.item.SimpleGeoItemRenderer;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import software.bernie.geckolib.model.DefaultedItemGeoModel;


public class TGUtil {
    public static void registerOtherGunModel(RegisterClientExtensionsEvent event, String modid, DeferredItem<? extends BaseGun> gunSupplier) {
        registerGunModel(event, ResourceLocation.fromNamespaceAndPath(modid, "gun/" + gunSupplier.getId().getPath()), gunSupplier);
    }

    public static void registerGunModel(RegisterClientExtensionsEvent event, DeferredItem<? extends BaseGun> gunSupplier) {
        registerGunModel(event, ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, "gun/" + gunSupplier.getId().getPath()), gunSupplier);
    }

    public static void registerGunModel(RegisterClientExtensionsEvent event, ResourceLocation resourceLocation, DeferredItem<? extends BaseGun> gunSupplier) {
        event.registerItem(new SimpleGeoItemRenderer<BaseGun>(new DefaultedItemGeoModel<>(resourceLocation)), gunSupplier.get());
    }

    public static float criticalDamageTotal(float critical, float damage){
        if (RandomSource.create().nextFloat() < critical) {
            return damage * 2;
        }
        return damage;
    }
}
