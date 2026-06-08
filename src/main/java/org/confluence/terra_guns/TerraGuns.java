package org.confluence.terra_guns;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_guns.client.event.TGGameClientEvent;
import org.confluence.terra_guns.client.event.TGModClientEvent;
import org.confluence.terra_guns.common.event.TGGameEvent;
import org.confluence.terra_guns.common.event.TGModEvent;
import org.confluence.terra_guns.common.init.*;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraGuns.MODID)
public class TerraGuns {
    public static final String MODID = "terra_guns";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Guns");
    public static final PortNetworkHandler NETWORK_HANDLER = new PortNetworkHandler(MODID, "1");

    public TerraGuns(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        TGDataComponents.init();
        TGEntities.ENTITY_TYPES.register(eventBus);
        TGItems.GUNS.register(eventBus);
        TGItems.BULLETS.register(eventBus);
        TGItems.OTHER.register(eventBus);
        TGSoundEvents.SOUNDS.register(eventBus);
        if (!ConfluenceMagicLib.IS_CONFLUENCE_LOAD) {
            TGTabs.TABS.register(eventBus);
        }

        TGModEvent.init();
        TGGameEvent.init();
        if (LibUtils.isPhysicalClient()) {
            TGModClientEvent.init();
            TGGameClientEvent.init();
        }
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String asResourceString(String path) {
        return asResource(path).toString();
    }
}
