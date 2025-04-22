package org.confluence.terra_guns.client.event;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.renderer.entity.BulletRenderer;
import org.confluence.terra_guns.client.renderer.item.SimpleGeoItemRenderer;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.util.TGUtil;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

import static org.confluence.terra_guns.client.init.TGKeys.*;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvent {
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        TGItems.GUNS.getEntries().forEach(holder -> TGUtil.registerGunModel(event, (DeferredItem<BaseGun>) holder));
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TGEntities.BASE_BULLET_ENTITY.get(), BulletRenderer::new);
    }

    @SubscribeEvent
    public static void keyBinding(RegisterKeyMappingsEvent event) {
        event.register(SHOOT.get());
        event.register(AIM.get());
    }
}
