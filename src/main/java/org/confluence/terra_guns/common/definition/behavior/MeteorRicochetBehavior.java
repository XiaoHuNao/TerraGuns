package org.confluence.terra_guns.common.definition.behavior;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public final class MeteorRicochetBehavior extends AbstractBulletBehavior {
    public static final MeteorRicochetBehavior INSTANCE = new MeteorRicochetBehavior();
    private static final int MAX_BOUNCES = 1;
    private static final int BOUNCED_STATE = 1;
    private static final int PIERCED_STATE = 2;
    private static final double DIRECTION_EPSILON = 1.0E-6D;

    private MeteorRicochetBehavior() {
        super("tooltip.terra_guns.ability.meteor_ricochet");
    }

    @Override
    public boolean onHitBlock(BaseBulletEntity entity, BlockHitResult result) {
        if (entity.getEffectState() >= MAX_BOUNCES) return false;

        Vec3 velocity = entity.getDeltaMovement();
        Direction direction = result.getDirection();
        Vec3 normal = new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ());
        double dot = velocity.dot(normal);
        if (dot >= -DIRECTION_EPSILON) return false;

        Vec3 reflected = velocity.subtract(normal.scale(2.0D * dot));
        if (reflected.lengthSqr() < 1.0E-5D) return false;

        entity.setEffectState(BOUNCED_STATE);
        entity.setDeltaMovement(reflected);
        BulletBehaviorSupport.moveOutsideBlock(entity, result);
        return true;
    }

    @Override
    public void onHitEntity(BaseBulletEntity entity, EntityHitResult result) {
        if (entity.getEffectState() == BOUNCED_STATE) {
            entity.setPenetrate(1);
        } else if (entity.getEffectState() == 0) {
            entity.setEffectState(PIERCED_STATE);
            int penetrate = entity.getPenetrate();
            entity.setPenetrate(penetrate < 0 ? 2 : Math.min(penetrate, 2));
        }
    }
}
