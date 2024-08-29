package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import org.confluence.terra_guns.TerraGuns;

public class ModTags {

    public static class DamageTypes {
        public static final TagKey<DamageType> RANGED_ATTACK = TagKey.create(Registries.DAMAGE_TYPE, TerraGuns.asResource("ranged_attack"));
    }
}
