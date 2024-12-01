package org.confluence.terra_guns;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.confluence.terra_guns.common.init.TGAttributes;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.init.TGItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraGuns.MODID)
public class TerraGuns {
    public static final String MODID = "terra_guns";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Guns");

    public TerraGuns(IEventBus modEventBus, ModContainer modContainer) {
        TGItems.ITEM_GUNS.register(modEventBus);
        TGItems.ITEM_BULLETS.register(modEventBus);
        TGItems.Tab.CREATIVE_MODE_TAB.register(modEventBus);
        TGEntities.ENTITIES.register(modEventBus);
        TGAttributes.ATTRIBUTES.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
