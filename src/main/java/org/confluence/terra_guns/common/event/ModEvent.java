package org.confluence.terra_guns.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.network.c2s.ShootPacketC2S;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvent {
    @SubscribeEvent
    public static void registerNetWork(RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ShootPacketC2S.TYPE, ShootPacketC2S.STREAM_CODEC, ShootPacketC2S::handle);
    }
}
