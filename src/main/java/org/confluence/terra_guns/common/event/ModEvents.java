package org.confluence.terra_guns.common.event;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGAttributes;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER, TGAttributes.AMMO_CONSUME_RATE)){
            event.add(EntityType.PLAYER, TGAttributes.AMMO_CONSUME_RATE);
        }
    }
}
