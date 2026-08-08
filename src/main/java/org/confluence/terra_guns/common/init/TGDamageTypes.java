package org.confluence.terra_guns.common.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.confluence.lib.common.LibDamageTypes;
import org.jetbrains.annotations.ApiStatus;

@Deprecated(since = "1.3.0", forRemoval = true)
@ApiStatus.ScheduledForRemoval(inVersion = "1.4.0")
public class TGDamageTypes {
    public static final ResourceKey<DamageType> BULLET_DAMAGE = LibDamageTypes.GUN_BULLET;

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return LibDamageTypes.of(level, key);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing) {
        return LibDamageTypes.of(level, key, causing);
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity causing, Entity direct) {
        return LibDamageTypes.of(level, key, causing, direct);
    }
}
