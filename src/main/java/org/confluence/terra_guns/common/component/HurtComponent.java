package org.confluence.terra_guns.common.component;

import com.google.common.collect.BiMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;

public class HurtComponent implements IHit {
    public static final ResourceLocation REGISTRY_NAME = TerraGuns.asResource("hurt");
    private boolean canBreakBlock = false;

    public HurtComponent(boolean canBreakBlock) {
        this.canBreakBlock = canBreakBlock;
    }

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
    public void onHitBlock(Projectile projectile, BlockHitResult pResult) {
        if (canBreakBlock) {
            projectile.level().destroyBlock(pResult.getBlockPos(), true);
        }else {
            BlockState blockstate = projectile.level().getBlockState(pResult.getBlockPos());
            blockstate.onProjectileHit(projectile.level(), blockstate, pResult, projectile);
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return REGISTRY_NAME;
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

    public HurtComponent setCanBreakBlock(boolean canBreakBlock) {
        this.canBreakBlock = canBreakBlock;
        return this;
    }
}
