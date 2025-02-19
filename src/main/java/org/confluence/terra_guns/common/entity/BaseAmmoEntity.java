package org.confluence.terra_guns.common.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.api.IAmmo;
import org.confluence.terra_guns.common.component.HurtComponent;
import org.confluence.terra_guns.common.component.IHit;
import org.confluence.terra_guns.common.component.PierceComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseAmmoEntity extends AbstractHurtingProjectile {
    private ItemStack ammoStack = ItemStack.EMPTY;
    private final BiMap<ResourceLocation, Pair<Integer, IHit>> hits = HashBiMap.create();
    private float damage;
    private float knockback;
    private final List<Vec3> trails = new LinkedList<>();
    protected Vec3 posO = Vec3.ZERO;

    public BaseAmmoEntity(EntityType<? extends AbstractHurtingProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        registerHits();
    }

    public BaseAmmoEntity(EntityType<? extends AbstractHurtingProjectile> pEntityType, LivingEntity pShooter, Vec3 movement, Level pLevel) {
        super(pEntityType, pShooter, movement, pLevel);
        setPos(pShooter.getX(), pShooter.getEyeY() - 0.1, pShooter.getZ());
        registerHits();
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            if (trails.isEmpty()) {
                trails.add(this.position());
            }
            trails.add(this.position());
            if (trails.size() > 3 || posO == this.position()) {
                trails.removeFirst();
            }
            posO = this.position();
        } else {
            Entity owner = this.getOwner();
            if (owner != null && owner.position().distanceTo(this.position()) > 256) {
                this.discard();
            }
        }
        super.tick();
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target.canBeHitByProjectile() && target != getOwner();
    }

    public List<Vec3> getTrails() {
        return trails;
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

    public IHit getHits(ResourceLocation registryName) {
        return hits.get(registryName).getSecond();
    }

    public void addHit(int pPriority, IHit hit) {
        hits.put(hit.getRegistryName(), Pair.of(pPriority, hit));
    }

    public void registerHits() {
        addHit(0, new HurtComponent(false));
        addHit(1, new PierceComponent(1, false));
    }

    public void doPostHurtEffects(Entity target) {
        if (ammoStack.getItem() instanceof IAmmo bullet) {
            bullet.doPostHurtEffects(this, target);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        processHits(hit -> hit.onHitEntity(this, result));
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        processHits(hit -> hit.onHitBlock(this, result));
    }

    private void processHits(Consumer<IHit> hitAction) {
        List<Pair<Integer, IHit>> hitList = Lists.newArrayList(hits.values());
//        hitList.sort(Comparator.comparingInt(Pair::getFirst));
        hitList.sort(Collections.reverseOrder(Comparator.comparingInt(Pair::getFirst)));

        for (Pair<Integer, IHit> hit : hitList) {
            IHit hit1 = hit.getSecond();
            if (hit1.hasConflict(hits, hit)) {
                hitAction.accept(hit1);
            }
        }
    }

    public void setPiece(int piece, boolean canPierceBlock) {
        IHit second = hits.get(PierceComponent.REGISTRY_NAME).getSecond();
        if (second instanceof PierceComponent pierceComponent) {
            pierceComponent.setMaxPierce(piece).setCanPierceBlock(canPierceBlock);
        }
    }

    public void setCanBreakBlock(boolean canBreakBlock) {
        IHit second = hits.get(HurtComponent.REGISTRY_NAME).getSecond();
        if (second instanceof HurtComponent hurtComponent) {
            hurtComponent.setCanBreakBlock(canBreakBlock);
        }
    }

    @Nullable
    @Override
    protected ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}
