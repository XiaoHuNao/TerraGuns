package org.confluence.terra_guns.impl;

import net.minecraft.util.RandomSource;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;

public class AmmoDataContext {
    private final GunPropertyComponent gunComponent;
    private final BulletPropertyComponent bulletComponent;
    private final float inaccuracy;

    public AmmoDataContext(GunPropertyComponent gunComponent, BulletPropertyComponent bulletComponent, float inaccuracy) {
        this.gunComponent = gunComponent;
        this.bulletComponent = bulletComponent;
        this.inaccuracy = inaccuracy;
    }

    public float getDamage() {
        float damage = gunComponent.damage() + bulletComponent.damage();
        if (RandomSource.create().nextFloat() < gunComponent.critical() + 0.31f) {
            return damage * 2;
        }
        return damage;
    }

    public float getVelocity() {
        return (gunComponent.velocity() + bulletComponent.velocity()) * bulletComponent.velocityMultiplier();
    }

    public float getKnockback() {
        return gunComponent.knockback() + bulletComponent.knockback();
    }

    public int getPenetrate() {
        if (bulletComponent.penetrate() == -1 || gunComponent.penetrate() == -1) return -1;
        return gunComponent.penetrate() + bulletComponent.penetrate();
    }

    public float getInaccuracy() {
        return inaccuracy;
    }
}
