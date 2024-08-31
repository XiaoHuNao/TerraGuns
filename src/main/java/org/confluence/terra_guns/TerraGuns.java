package org.confluence.terra_guns;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraGuns.MOD_ID)
public class TerraGuns {
    public static final String MOD_ID = "terra_guns";
    public static final Logger LOGGER = LoggerFactory.getLogger("Confluence");
    public TerraGuns(IEventBus modEventBus, ModContainer modContainer) {
//        NeoForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
