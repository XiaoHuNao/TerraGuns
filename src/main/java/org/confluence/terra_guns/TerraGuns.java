package org.confluence.terra_guns;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.confluence.terra_guns.common.init.ModAttributes;
import org.confluence.terra_guns.common.init.ModEntities;
import org.confluence.terra_guns.common.init.ModItems;
import org.slf4j.Logger;

@Mod(TerraGuns.MODID)
public class TerraGuns {
    public static final String MODID = "terra_guns";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TerraGuns(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEM_GUNS.register(modEventBus);
        ModItems.ITEM_BULLETS.register(modEventBus);
        ModItems.Tab.CREATIVE_MODE_TAB.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
