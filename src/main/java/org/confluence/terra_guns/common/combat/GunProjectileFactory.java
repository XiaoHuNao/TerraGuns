package org.confluence.terra_guns.common.combat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.common.definition.GunProjectilePattern;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.entity.bullet.CustomBulletEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates projectile entities from already-resolved shot data.
 *
 * <p>Gun items do not know about entity constructors. That makes a new firing
 * pattern a combat concern instead of another Item subclass.</p>
 */
public final class GunProjectileFactory {
    private GunProjectileFactory() {
    }

    public static int spawn(ShotContext context, GunProjectilePattern pattern) {
        List<BaseBulletEntity> projectiles = create(context, pattern);
        projectiles.forEach(context.level()::addFreshEntity);
        return projectiles.size();
    }

    public static List<BaseBulletEntity> create(ShotContext context, GunProjectilePattern pattern) {
        int count = pattern.type() == GunProjectilePattern.Type.SHOTGUN
                ? pattern.sampleProjectileCount(context.shooter().getRandom())
                : 1;
        List<BaseBulletEntity> projectiles = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            projectiles.add(createProjectile(context, pattern));
        }
        return projectiles;
    }

    private static BaseBulletEntity createProjectile(ShotContext context, GunProjectilePattern pattern) {
        ServerPlayer shooter = context.shooter();
        ItemStack ammo = context.ammo();
        BaseBulletEntity entity = pattern.type() == GunProjectilePattern.Type.GRAVITY
                ? new CustomBulletEntity(shooter, pattern.gravity(), ammo)
                : new BaseBulletEntity(shooter, ammo);

        entity.setDamage(context.damage());
        entity.setKnockback(context.knockback());
        entity.setPenetrate(context.penetrate());
        Vec3 direction = shooter.getViewVector(1.0F);
        float speed = Math.max(0.0F, context.velocity());
        entity.shoot(
                direction.x,
                direction.y,
                direction.z,
                speed,
                Math.max(0.0F, context.inaccuracy())
        );
        entity.setInitialVelocity(entity.getDeltaMovement());
        // Start just in front of the muzzle. This prevents the first swept
        // collision from touching the shooter or a block occupying the eye.
        entity.setPos(entity.position().add(direction.scale(0.18D)));
        return entity;
    }
}
