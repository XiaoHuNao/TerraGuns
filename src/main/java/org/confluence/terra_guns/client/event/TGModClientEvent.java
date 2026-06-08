package org.confluence.terra_guns.client.event;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import org.confluence.terra_guns.client.renderer.entity.BulletRenderer;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.util.TGUtil;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortEntityRenderersEvent;
import org.mesdag.portlib.event.client.PortRegisterKeyMappingsEvent;
import org.mesdag.portlib.event.client.extensions.common.PortRegisterClientExtensionsEvent;

import static org.confluence.terra_guns.client.init.TGKeys.AIM;
import static org.confluence.terra_guns.client.init.TGKeys.SHOOT;

public class TGModClientEvent {
    public static void init() {
        PortEventHandler.addListener(TGModClientEvent::registerClientExtensions);
        PortEventHandler.addListener(TGModClientEvent::registerEntityRenderers);
        PortEventHandler.addListener(TGModClientEvent::keyBinding);
    }

    private static void registerClientExtensions(PortRegisterClientExtensionsEvent event) {
        TGItems.GUNS.getEntries().forEach(holder -> TGUtil.registerGunModel(event, holder));
    }

    private static void registerEntityRenderers(PortEntityRenderersEvent.PortRegisterRenderers event) {
        event.registerEntityRenderer(TGEntities.BASE_BULLET_ENTITY.get(), BulletRenderer::new);
        event.registerEntityRenderer(TGEntities.GRAVITY_BULLET_ENTITY.get(), ThrownItemRenderer::new);
    }

    private static void keyBinding(PortRegisterKeyMappingsEvent event) {
        event.register(SHOOT.get());
        event.register(AIM.get());
    }
}
