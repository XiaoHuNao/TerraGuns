package org.confluence.terra_guns.common.entity;

import com.google.common.collect.Lists;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.api.IBullet;
import org.confluence.terra_guns.common.component.HurtComponent;
import org.confluence.terra_guns.common.component.IHit;
import org.confluence.terra_guns.common.init.ModEntities;

import java.util.List;

public abstract class BaseAmmoEntity extends AbstractHurtingProjectile {
    private ItemStack ammoStack = ItemStack.EMPTY;
    private final List<IHit> hits = Lists.newArrayList();
    private float damage;
    private float knockback;

    public BaseAmmoEntity(EntityType<? extends AbstractHurtingProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        addHit(new HurtComponent());
    }
    public BaseAmmoEntity(EntityType<? extends AbstractHurtingProjectile> pEntityType, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
        super(pEntityType, pShooter, pOffsetX, pOffsetY, pOffsetZ, pLevel);
        setPos(pShooter.getX(), pShooter.getEyeY() - 0.1, pShooter.getZ());
        addHit(new HurtComponent());
    }

    public ItemStack getAmmoStack() {
        return ammoStack;
    }

    public BaseAmmoEntity setAmmoStack(ItemStack ammoStack) {
        this.ammoStack = ammoStack;
        return this;
    }

    public float getDamage() {
        return damage;
    }

    public BaseAmmoEntity setDamage(float damage) {
        this.damage = damage;
        return this;
    }

    public float getKnockback() {
        return knockback;
    }

    public BaseAmmoEntity setKnockback(float knockback) {
        this.knockback = knockback;
        return this;
    }
    public BaseAmmoEntity damageAndKnockback(float damage, float knockback) {
        this.damage = damage;
        this.knockback = knockback;
        return this;
    }

    public List<IHit> getHits() {
        return hits;
    }
    public void addHit(IHit hit) {
        hits.add(hit);
    }
    public void registerHits() {
    }

    public void doPostHurtEffects(Entity target) {
        if (ammoStack.getItem() instanceof IBullet bullet) {
            bullet.doPostHurtEffects(this, target);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        for (IHit hit : hits) {
            hit.onHitEntity(this, pResult);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        for (IHit hit : hits) {
            hit.onHitBlock(this, pResult);
        }
    }
}
