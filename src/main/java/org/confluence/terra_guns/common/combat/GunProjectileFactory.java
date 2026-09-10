package org.confluence.terra_guns.common.combat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.enchantment.GunEnchantmentService;
import org.confluence.terra_guns.common.definition.GunProjectilePattern;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.entity.bullet.CustomBulletEntity;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<Projectile> projectiles = new ArrayList<>(createDefaults(context, pattern));
        BaseGun gun = (BaseGun) context.gun().getItem();
        GunEvent.ProjectileCreationEvent event = new GunEvent.ProjectileCreationEvent(gun, context, projectiles);
        NeoForge.EVENT_BUS.post(event);

        projectiles = event.getProjectiles();
        projectiles.removeIf(Objects::isNull);
        projectiles.forEach(projectile -> configureProjectile(context, projectile));
        projectiles.forEach(context.level()::addFreshEntity);
        return projectiles.size();
    }

    public static int spawnRadial(ShotContext context, GunProjectilePattern pattern, int directions) {
        int count = Math.max(1, directions);
        for (int index = 0; index < count; index++) {
            double angle = Math.PI * 2.0D * index / count;
            Vec3 direction = new Vec3(Math.cos(angle), 0.0D, Math.sin(angle));
            context.level().addFreshEntity(createProjectile(context, pattern, direction));
        }
        return count;
    }

    public static List<BaseBulletEntity> create(ShotContext context, GunProjectilePattern pattern) {
        List<BaseBulletEntity> projectiles = createDefaults(context, pattern);
        projectiles.forEach(projectile -> configureProjectile(context, projectile));
        return projectiles;
    }

    private static List<BaseBulletEntity> createDefaults(ShotContext context, GunProjectilePattern pattern) {
        int count = pattern.type() == GunProjectilePattern.Type.SHOTGUN
                ? pattern.sampleProjectileCount(context.shooter().getRandom())
                : 1;
        List<BaseBulletEntity> projectiles = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            projectiles.add(createDefaultProjectile(context, pattern));
        }
        return projectiles;
    }

    private static BaseBulletEntity createDefaultProjectile(ShotContext context, GunProjectilePattern pattern) {
        ServerPlayer shooter = context.shooter();
        return createProjectile(context, pattern, shooter.getViewVector(1.0F));
    }

    private static BaseBulletEntity createProjectile(ShotContext context, GunProjectilePattern pattern, Vec3 direction) {
        ServerPlayer shooter = context.shooter();
        ItemStack ammo = context.ammo();
        return pattern.type() == GunProjectilePattern.Type.GRAVITY
                ? new CustomBulletEntity(shooter, pattern.gravity(), ammo)
                : new BaseBulletEntity(shooter, ammo);
    }

    private static void configureProjectile(ShotContext context, Projectile projectile) {
        ServerPlayer shooter = context.shooter();
        float speed = Math.max(0.0F, context.velocity());
        float inaccuracy = Math.max(0.0F, context.inaccuracy());
        projectile.setOwner(shooter);

        if (!(projectile instanceof BaseBulletEntity entity)) {
            projectile.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
            projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed, inaccuracy);
            return;
        }

        entity.setDamage(context.damage());
        entity.setKnockback(context.knockback());
        entity.setPenetrate(context.penetrate());
        entity.setTemporaryReserveLevel(
                GunEnchantmentService.getTemporaryReserveLevel(shooter, context.gun())
        );
        float speed = Math.max(0.0F, context.velocity());
        Vec3 direction = shooter.getViewVector(1.0F);
        entity.shoot(
                direction.x,
                direction.y,
                direction.z,
                speed,
                inaccuracy
        );
        entity.setInitialVelocity(entity.getDeltaMovement());
        // Start just in front of the muzzle. This prevents the first swept
        // collision from touching the shooter or a block occupying the eye.
        entity.setPos(entity.position().add(direction.scale(0.18D)));
    }
}
