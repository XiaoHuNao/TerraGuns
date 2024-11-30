package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, TerraGuns.MODID);
    public static final DeferredHolder<Attribute, Attribute> AMMO_CONSUME_RATE = ATTRIBUTES.register("ammo_consume_rate", () -> new RangedAttribute("attribute.name.generic.ammo_consume_rate", 0.0D, 0.0D, 1.0D));
}
