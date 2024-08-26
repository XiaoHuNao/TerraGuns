package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_guns.TerraGuns;

@Mod.EventBusSubscriber
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, TerraGuns.MODID);
    public static final RegistryObject<RangedAttribute> AMMO_CONSUME_RATE = ATTRIBUTES.register("ammo_consume_rate", () -> new RangedAttribute("attribute.name.generic.ammo_consume_rate", 0.0D, 0.0D, 1.0D));

}
