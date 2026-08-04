package org.confluence.terra_guns.common.definition.behavior;

import net.minecraft.world.phys.EntityHitResult;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.init.TGMobEffects;

public final class IchorDebuffBehavior extends AbstractBulletBehavior {
    public static final IchorDebuffBehavior INSTANCE = new IchorDebuffBehavior();

    private IchorDebuffBehavior() {
        super("tooltip.terra_guns.ability.ichor_debuff");
    }

    @Override
    public void onHitEntity(BaseBulletEntity entity, EntityHitResult result) {
        BulletBehaviorSupport.applyEffect(result.getEntity(), TGMobEffects.ICHOR, 240, 0);
    }
}
