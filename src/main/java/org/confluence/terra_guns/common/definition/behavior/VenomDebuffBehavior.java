package org.confluence.terra_guns.common.definition.behavior;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.EntityHitResult;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public final class VenomDebuffBehavior extends AbstractBulletBehavior {
    public static final VenomDebuffBehavior INSTANCE = new VenomDebuffBehavior();

    private VenomDebuffBehavior() {
        super("tooltip.terra_guns.ability.venom_debuff");
    }

    @Override
    public void onHitEntity(BaseBulletEntity entity, EntityHitResult result) {
        BulletBehaviorSupport.applyEffect(result.getEntity(), MobEffects.POISON, 160, 0);
    }
}
