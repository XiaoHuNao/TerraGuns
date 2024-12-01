package org.confluence.terra_guns.common.event;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.ModAttributes;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvent {
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER, ModAttributes.AMMO_CONSUME_RATE)){
            event.add(EntityType.PLAYER,ModAttributes.AMMO_CONSUME_RATE);
        }
    }
}
