package org.confluence.terra_guns.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.renderer.entity.SimpleItemModelProjectileRenderer;
import org.confluence.terra_guns.common.init.TGEntities;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TGEntities.SIMPLE_ITEM_MODEL_PROJECTILE.get(), SimpleItemModelProjectileRenderer::new);
    }
}
