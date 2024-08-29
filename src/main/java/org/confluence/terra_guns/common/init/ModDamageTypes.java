package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.TerraGuns;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> RANGED_ATTACK = register("ranged_attack");

    private static ResourceKey<DamageType> register(String id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, TerraGuns.asResource(id));
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return of(level, key, null, null);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing) {
        return of(level, key, causing, causing);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing, Entity direct) {
        return new DamageSource(level.registryAccess().registry(Registries.DAMAGE_TYPE).orElseThrow().getHolderOrThrow(key), causing, direct);
    }
}
