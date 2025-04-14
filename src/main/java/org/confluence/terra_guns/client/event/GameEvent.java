package org.confluence.terra_guns.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.confluence.terra_guns.TerraGuns;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameEvent {
    @SubscribeEvent
    public static void cancelSwap(PlayerInteractEvent.LeftClickEmpty event){

    }
}
