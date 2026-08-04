package org.confluence.terra_guns.common.definition.behavior;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public final class CrystalSplitBehavior extends AbstractBulletBehavior {
    public static final CrystalSplitBehavior INSTANCE = new CrystalSplitBehavior();
    private static final double SHARD_SPEED_MULTIPLIER = 0.35D;
    private static final double SHARD_MAX_INITIAL_SPEED = 6.0D;
    private static final double SHARD_MIN_INITIAL_SPEED = 0.35D;

    private CrystalSplitBehavior() {
        super("tooltip.terra_guns.ability.crystal_split");
    }

    @Override
    public boolean onHitBlock(BaseBulletEntity entity, BlockHitResult result) {
        Direction direction = result.getDirection();
        split(entity, new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ()).scale(0.12D));
        return false;
    }

    @Override
    public void onHitEntity(BaseBulletEntity entity, EntityHitResult result) {
        Vec3 velocity = entity.getDeltaMovement();
        Vec3 offset = velocity.lengthSqr() > 1.0E-5D ? velocity.normalize().scale(-0.12D) : Vec3.ZERO;
        split(entity, offset);
    }

    private static void split(BaseBulletEntity entity, Vec3 spawnOffset) {
        if (entity.level().isClientSide || entity.getEffectState() > 0) return;

        Vec3 velocity = entity.getDeltaMovement();
        if (velocity.lengthSqr() < 1.0E-5D) {
            velocity = entity.getLookAngle();
        }
        if (velocity.lengthSqr() < 1.0E-5D) return;

        double shardSpeed = Math.min(
                SHARD_MAX_INITIAL_SPEED,
                Math.max(SHARD_MIN_INITIAL_SPEED, velocity.length() * SHARD_SPEED_MULTIPLIER)
        );
        Vec3 direction = velocity.normalize().scale(-1.0D);
        for (int index = -1; index <= 1; index += 2) {
            Vec3 shardDirection = rotateAroundY(direction, index * 0.32D);
            BaseBulletEntity shard = entity.createChild(shardDirection.scale(shardSpeed), 0.5F, 1, spawnOffset);
            shard.setIgnoresBlockCollision(true);
            entity.level().addFreshEntity(shard);
        }
    }

    private static Vec3 rotateAroundY(Vec3 vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vec3(
                vector.x * cos + vector.z * sin,
                vector.y,
                vector.z * cos - vector.x * sin
        ).normalize();
    }
}
