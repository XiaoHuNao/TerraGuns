package org.confluence.terra_guns.util;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.renderer.item.SimpleGeoItemRenderer;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import software.bernie.geckolib.model.DefaultedItemGeoModel;


public class TGUtil {
    public static void registerGunModel(RegisterClientExtensionsEvent event, DeferredItem<BaseGun> gunSupplier){
        registerGunModel(event, ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, "gun/" + gunSupplier.getId().getPath()), gunSupplier);
    }

    public static void registerGunModel(RegisterClientExtensionsEvent event, ResourceLocation resourceLocation, DeferredItem<BaseGun> gunSupplier){
        event.registerItem(new SimpleGeoItemRenderer<BaseGun>(new DefaultedItemGeoModel<>(resourceLocation)), gunSupplier.get());
    }
}
