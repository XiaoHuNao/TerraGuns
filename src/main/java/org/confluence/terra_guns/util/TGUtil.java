package org.confluence.terra_guns.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.lib.util.LibMathUtils;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.renderer.item.SimpleGeoItemRenderer;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.mesdag.portlib.event.client.extensions.common.PortRegisterClientExtensionsEvent;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class TGUtil {
    public static void registerOtherGunModel(PortRegisterClientExtensionsEvent event, String modid, RegistryObject<? extends Item> gunSupplier) {
        registerGunModel(event, ResourceLocation.fromNamespaceAndPath(modid, "gun/" + gunSupplier.getId().getPath()), gunSupplier);
    }

    public static void registerGunModel(PortRegisterClientExtensionsEvent event, RegistryObject<? extends Item> gunSupplier) {
        registerGunModel(event, ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, "gun/" + gunSupplier.getId().getPath()), gunSupplier);
    }

    public static void registerGunModel(PortRegisterClientExtensionsEvent event, ResourceLocation resourceLocation, RegistryObject<? extends Item> gunSupplier) {
        event.registerItem(new SimpleGeoItemRenderer<BaseGun>(new DefaultedItemGeoModel<>(resourceLocation)), gunSupplier.get());
    }

    public static float criticalDamageTotal(float critical, float damage, RandomSource random) {
        return LibMathUtils.checkChance(critical, random) ? damage * 1.5F : damage;
    }
}
