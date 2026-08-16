package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.lib.common.effect.PublicMobEffect;
import org.confluence.terra_guns.TerraGuns;

public class TGMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TerraGuns.MODID);

    //TODO REMOVE WHEN CONFLUENCE
    public static final DeferredHolder<MobEffect, MobEffect> ICHOR = MOB_EFFECTS.register("ichor", () ->
            new PublicMobEffect(MobEffectCategory.HARMFUL, 0xF0A000)
                    .addAttributeModifier(
                            Attributes.ARMOR,
                            TerraGuns.asResource("ichor_armor"),
                            -0.15D,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    ));

    public static final DeferredHolder<MobEffect, MobEffect> CURSED_INFERNO = MOB_EFFECTS.register("cursed_inferno", () ->
            new PublicMobEffect(MobEffectCategory.HARMFUL, 0x56C91B) {
                @Override
                public boolean applyEffectTick(LivingEntity living, int amplifier) {
                    if (!living.level().isClientSide) {
                        living.hurt(living.damageSources().magic(), 1.0F + amplifier);
                    }
                    return true;
                }

                @Override
                public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
                    return duration % 20 == 0;
                }
            });

    public static final DeferredHolder<MobEffect, MobEffect> VENOM = MOB_EFFECTS.register("venom", () ->
            new PublicMobEffect(MobEffectCategory.HARMFUL, 0x8A45A8) {
                @Override
                public boolean applyEffectTick(LivingEntity living, int amplifier) {
                    if (!living.level().isClientSide) {
                        living.hurt(living.damageSources().magic(), 1.5F + amplifier);
                    }
                    return true;
                }

                @Override
                public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
                    return duration % 10 == 0;
                }
            });
}
