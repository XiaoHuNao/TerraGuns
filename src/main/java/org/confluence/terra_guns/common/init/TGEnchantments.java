package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.confluence.terra_guns.TerraGuns;

/** Resource keys for Terra Guns' data-driven enchantments. */
public final class TGEnchantments {
    public static final ResourceKey<Enchantment> EMERGENCY_MELEE = key("emergency_melee");
    public static final ResourceKey<Enchantment> TEMPORARY_RESERVE = key("temporary_reserve");
    public static final ResourceKey<Enchantment> COMPRESSED_TACTICS = key("compressed_tactics");

    private TGEnchantments() {
    }

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(
                Registries.ENCHANTMENT,
                ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, name)
        );
    }
}
