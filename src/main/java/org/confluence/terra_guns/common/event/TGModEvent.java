package org.confluence.terra_guns.common.event;

import org.confluence.lib.api.event.NameFixRegisterEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGGunSounds;
import org.confluence.terra_guns.common.init.TGTrailColors;
import org.confluence.terra_guns.network.c2s.ShootPacketC2S;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.lifecycle.PortFMLCommonSetupEvent;

public class TGModEvent {
    public static void init() {
        registerNetWork();
        PortEventHandler.addListener(TGModEvent::fmlCommonSetup);
        PortEventHandler.addListener(TGModEvent::nameFixRegister$Item);
    }

    private static void registerNetWork() {
        TerraGuns.NETWORK_HANDLER.registerInGameC2S(ShootPacketC2S.class, ShootPacketC2S.ID, ShootPacketC2S.STREAM_CODEC);
    }

    private static void fmlCommonSetup(PortFMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TGGunSounds.init();
            TGTrailColors.init();
        });
    }

    private static void nameFixRegister$Item(NameFixRegisterEvent.Item event) {
        // 1.1.4 -> 1.1.5
        event.register("terra_guns:blowpipe", "terra_guns:blowgun");
    }
}
