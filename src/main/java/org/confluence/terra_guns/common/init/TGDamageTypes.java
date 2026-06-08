package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_guns.TerraGuns;

public class TGDamageTypes {
    public static final ResourceKey<DamageType> BULLET_DAMAGE = register("bullet_damage");

    private static ResourceKey<DamageType> register(String id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, id));
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return LibUtils.damageSource(level, key, null, null);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing) {
        return LibUtils.damageSource(level, key, causing, causing);
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(BULLET_DAMAGE, new DamageType("bullet_damage", DamageScaling.NEVER, 0.1F));
    }
}
