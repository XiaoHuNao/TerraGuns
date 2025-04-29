package org.confluence.terra_guns.common.item.gun;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.common.entity.bullet.CustomBulletEntity;

import java.util.List;

public class CustomGun extends BaseGun {
    private final float gravity;

    public CustomGun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, int penetrate, float inaccuracy, ModRarity rarity, float gravity) {
        super(properties, cooldown, damage, velocity, knockback, critical, penetrate, inaccuracy, rarity);
        this.gravity = gravity;
    }

    public CustomGun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, float inaccuracy, ModRarity rarity, float gravity) {
        super(properties, cooldown, damage, velocity, knockback, critical, inaccuracy, rarity);
        this.gravity = gravity;
    }

    @Override
    protected void prepareBulletEntity(List<Projectile> baseBulletEntities, ServerPlayer player, ItemStack bullet, ItemStack gun, float damage, float knockback, float velocity, int penetrate, float inaccuracy) {
        CustomBulletEntity baseBulletEntity = new CustomBulletEntity(player, gravity, bullet);

        baseBulletEntity.setColorID(((BaseGun) gun.getItem()).getColorID());
        baseBulletEntity.damage = damage;
        baseBulletEntity.knockback = knockback;
        baseBulletEntity.penetrate = penetrate;
        baseBulletEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, velocity, inaccuracy);

        baseBulletEntities.add(baseBulletEntity);
    }
}

