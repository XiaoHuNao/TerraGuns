package org.confluence.terra_guns.common.definition.behavior;

import net.minecraft.world.phys.EntityHitResult;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public final class HighVelocityDamageDecayBehavior extends AbstractBulletBehavior {
    public static final HighVelocityDamageDecayBehavior INSTANCE = new HighVelocityDamageDecayBehavior();
    private static final float DAMAGE_MULTIPLIER = 0.85F;
    private static final float MIN_DAMAGE = 0.01F;

    private HighVelocityDamageDecayBehavior() {
        super("tooltip.terra_guns.ability.high_velocity_damage_decay");
    }

    @Override
    public void onHitEntity(BaseBulletEntity entity, EntityHitResult result) {
        float nextDamage = entity.getDamage() * DAMAGE_MULTIPLIER;
        entity.setDamage(nextDamage < MIN_DAMAGE ? 0.0F : nextDamage);
    }
}
