package org.confluence.terra_guns.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.util.TGUtil;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvent {
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        TGUtil.registerGunModel(event, TGItems.ANIM_ITEM);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerEntityRenderer(TGEntities.BASE_BULLET_ENTITY.get(), BulletRenderer::new);
    }
}
