package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;

public final class TGAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, TerraGuns.MODID);

    public static final DeferredHolder<Attribute, Attribute> AMMO_CONSUME_CHANCE = ATTRIBUTES.register("player.ammo_consume_chance", () -> new PercentageAttribute("attribute.name.player.ammo_consume_chance", 1.0, 0.0, 1.0).setSyncable(true));
}
