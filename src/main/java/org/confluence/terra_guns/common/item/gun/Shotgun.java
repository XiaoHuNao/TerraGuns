package org.confluence.terra_guns.common.item.gun;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Shotgun extends BaseGun {
    private final int minBullet;
    private final int maxBullet;

    public Shotgun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, int penetrate, ModRarity rarity, int minBullet, int maxBullet) {
        super(properties, cooldown, damage, velocity, knockback, critical, penetrate, rarity);
        this.minBullet = minBullet;
        this.maxBullet = maxBullet;
    }

    public Shotgun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, ModRarity rarity, int minBullet, int maxBullet) {
        super(properties, cooldown, damage, velocity, knockback, critical, rarity);
        this.minBullet = minBullet;
        this.maxBullet = maxBullet;
    }

    @Override
    protected void prepareBulletEntity(List<BaseBulletEntity> baseBulletEntities, ServerPlayer player, ItemStack bullet, float damage, float knockback, float velocity, int penetrate) {
        int times = ThreadLocalRandom.current().nextInt(this.minBullet, this.maxBullet + 1);

        IntStream.range(0, times).forEach(i -> {
            BaseBulletEntity baseBulletEntity = new BaseBulletEntity(player);

            baseBulletEntity.setBullet((BaseBullet) bullet.getItem());
            baseBulletEntity.setDamage(damage);
            baseBulletEntity.setKnockback(knockback);
            baseBulletEntity.setPenetrate(penetrate);
            baseBulletEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, velocity, 10f);

            baseBulletEntities.add(baseBulletEntity);
        });
    }
}

