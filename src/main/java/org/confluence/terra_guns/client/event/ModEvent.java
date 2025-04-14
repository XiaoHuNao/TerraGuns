package org.confluence.terra_guns.client.event;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGEntities;

import static org.confluence.terra_guns.client.init.TGKeys.*;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvent {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TGEntities.BASE_BULLET_ENTITY.get(), NoopRenderer::new);
    }

    @SubscribeEvent
    public static void keyBinding(RegisterKeyMappingsEvent event) {
        event.register(SHOOT.get());
        event.register(AIM.get());
    }
}
