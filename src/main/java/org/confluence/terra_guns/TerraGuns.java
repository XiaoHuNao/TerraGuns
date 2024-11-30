package org.confluence.terra_guns;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.confluence.terra_guns.common.init.ModAttributes;
import org.confluence.terra_guns.common.init.ModEntities;
import org.confluence.terra_guns.common.init.ModItems;
import org.slf4j.Logger;

@Mod(TerraGuns.MODID)
public class TerraGuns {
    public static final String MODID = "terra_guns";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TerraGuns(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::onEntityAttributeModification);

        ModItems.ITEM_GUNS.register(modEventBus);
        ModItems.ITEM_BULLETS.register(modEventBus);
        ModItems.Tab.CREATIVE_MODE_TAB.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @SubscribeEvent
    public void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER,ModAttributes.AMMO_CONSUME_RATE)){
            event.add(EntityType.PLAYER,ModAttributes.AMMO_CONSUME_RATE);
        }
    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.SIMPLE_ITEM_MODEL_PROJECTILE.get(), NoopRenderer::new);
        }

        @SubscribeEvent
        public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        }
    }
}
