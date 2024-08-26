package org.confluence.terra_guns;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_guns.common.init.ModAttributes;
import org.confluence.terra_guns.common.init.ModItems;
import org.slf4j.Logger;

@Mod(TerraGuns.MODID)
public class TerraGuns {
    public static final String MODID = "terra_guns";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TerraGuns() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::onEntityAttributeModification);

        ModItems.ITEM_GUNS.register(modEventBus);
        ModItems.ITEM_BULLETS.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);


        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER,ModAttributes.AMMO_CONSUME_RATE.get())){
            event.add(EntityType.PLAYER,ModAttributes.AMMO_CONSUME_RATE.get());
        }
    }
}
