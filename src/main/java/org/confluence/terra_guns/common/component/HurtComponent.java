package org.confluence.terra_guns.common.component;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;
import org.confluence.terra_guns.common.init.ModDamageTypes;

public class HurtComponent implements IHit {
    //攻击实体
    @Override
    public void onHitEntity(Projectile projectile, EntityHitResult pResult) {
        Entity attack = projectile.getOwner();
        Entity target = pResult.getEntity();
        DamageSource damageSource = getDamageSource(projectile);


        if (projectile instanceof BaseAmmoEntity ammoEntity){
            float damage = ammoEntity.getDamage();
            float knockback = ammoEntity.getKnockback();
            if (target.hurt(damageSource, damage) && target instanceof LivingEntity livingEntity) {
                if (knockback > 0) {
                    double d0 = Math.max(0.0D, 1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = projectile.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)knockback * 0.6D * d0);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingEntity.push(vec3.x, 0.1D, vec3.z);
                    }
                }
            }
            ammoEntity.doPostHurtEffects(target);
        }
    }

    //挖掘方块
    @Override
    public void onHitBlock(Projectile entity, BlockHitResult pResult) {
        IHit.super.onHitBlock(entity, pResult);
    }

    private DamageSource getDamageSource(Projectile projectile) {
        Entity entity = projectile.getOwner();
        DamageSource damagesource;
        if (entity == null) {
//            damagesource = ModDamageTypes.of(projectile.level(),ModDamageTypes.RANGED_ATTACK, projectile);
            damagesource = projectile.damageSources().explosion(projectile,projectile);
        } else {
//            damagesource = ModDamageTypes.of(projectile.level(),ModDamageTypes.RANGED_ATTACK, entity, projectile);
            damagesource = projectile.damageSources().explosion(projectile,projectile);
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setLastHurtMob(entity);
            }
        }
        return damagesource;
    }


}
