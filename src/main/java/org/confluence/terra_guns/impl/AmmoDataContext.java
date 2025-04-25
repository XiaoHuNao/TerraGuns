package org.confluence.terra_guns.impl;

import net.minecraft.util.RandomSource;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;

public class AmmoDataContext {
    private final GunPropertyComponent gunComponent;
    private final BulletPropertyComponent bulletComponent;

    public AmmoDataContext(GunPropertyComponent gunComponent, BulletPropertyComponent bulletComponent){
        this.gunComponent = gunComponent;
        this.bulletComponent = bulletComponent;
    }

    public float getDamage(){
        float damage = gunComponent.damage() + bulletComponent.damage();
        if (RandomSource.create().nextFloat() < gunComponent.critical() + 0.31f){
            return damage * 2;
        }
        return damage;
    }

    public float getVelocity(){
        return (gunComponent.velocity() + bulletComponent.velocity()) * bulletComponent.velocityMultiplier();
    }

    public float getKnockback(){
        return gunComponent.knockback() + bulletComponent.knockback();
    }

    public int getPenetrate(){
        return gunComponent.penetrate() + bulletComponent.penetrate();
    }
}
