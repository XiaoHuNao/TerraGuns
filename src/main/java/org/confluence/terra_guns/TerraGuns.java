package org.confluence.terra_guns;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.confluence.terra_guns.common.init.TGItems.*;

@Mod(TerraGuns.MODID)
public class TerraGuns {
    public static final String MODID = "terra_guns";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Guns");
    public static final boolean IS_CONFLUENCE_LOADED = ModList.get().isLoaded("confluence");

    public TerraGuns(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String asResourceString(String path) {
        return asResource(path).toString();
    }
}
