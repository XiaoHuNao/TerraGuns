package org.confluence.terra_guns.client.event;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.model.item.GunItemModel;
import org.confluence.terra_guns.client.renderer.item.SimpleGeoItemRenderer;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.init.TGItems;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvent {
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("flintlock_pistol", true)), TGItems.FLINTLOCK_PISTOL.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("musket", true)), TGItems.MUSKET.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("the_undertaker", true)), TGItems.THE_UNDERTAKER.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("revolver", true)), TGItems.REVOLVER.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("minishark", true)), TGItems.MINISHARK.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("boomstick", true)), TGItems.BOOMSTICK.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("flare_gun", true)), TGItems.FLARE_GUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("bee_gun", true)), TGItems.BEE_GUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("blowgun", true)), TGItems.BLOWGUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("blowpipe", true)), TGItems.BLOWPIPE.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("handgun", true)), TGItems.HANGGUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("onyx_blaster", true)), TGItems.ONYX_BLASTER.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("phoenix_blaster", true)), TGItems.PHOENIX_BLASTER.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("sandgun", true)), TGItems.SANDGUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("slime_gun", true)), TGItems.SLIME_GUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("sniper_rifle", true)), TGItems.SNIPER_RIFLE.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("snowball_cannon", true)), TGItems.SNOWBALL_CANNON.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("space_gun", true)), TGItems.SPACE_GUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("star_cannon", true)), TGItems.STAR_CANNON.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("tactical_shotgun", true)), TGItems.TACTICAL_SHOTGUN.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("uzi", true)), TGItems.UZI.asItem());
        event.registerItem(new SimpleGeoItemRenderer<>(new GunItemModel("shotgun", true)), TGItems.SHOTGUN.asItem());
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TGEntities.SIMPLE_ITEM_MODEL_PROJECTILE.get(), NoopRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
}
