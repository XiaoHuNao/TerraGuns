package org.confluence.terra_guns.common.definition.behavior;

import net.minecraft.world.phys.EntityHitResult;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public final class ExplosiveBulletBehavior extends AbstractBulletBehavior {
    public static final ExplosiveBulletBehavior INSTANCE = new ExplosiveBulletBehavior();

    private ExplosiveBulletBehavior() {
        super("tooltip.terra_guns.ability.explosive");
    }

    @Override
    public boolean onHitBlock(BaseBulletEntity entity, net.minecraft.world.phys.BlockHitResult result) {
        BulletBehaviorSupport.explode(entity);
        return false;
    }

    @Override
    public void onHitEntity(BaseBulletEntity entity, EntityHitResult result) {
        BulletBehaviorSupport.explode(entity);
    }
}
