package org.confluence.terra_guns;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.terra_guns.common.init.ModAttributes;
import org.confluence.terra_guns.common.init.ModEntities;
import org.confluence.terra_guns.common.init.ModItems;
import org.slf4j.Logger;

@Mod(TerraGuns.MODID)
public class TerraGuns {
    public static final String MODID = "terra_guns";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TerraGuns() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::onEntityAttributeModification);

        ModItems.ITEM_GUNS.register(modEventBus);
        ModItems.ITEM_BULLETS.register(modEventBus);
        ModItems.Tab.CREATIVE_MODE_TAB.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);


        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    @SubscribeEvent
    public void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER,ModAttributes.AMMO_CONSUME_RATE.get())){
            event.add(EntityType.PLAYER,ModAttributes.AMMO_CONSUME_RATE.get());
        }
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
