package org.confluence.terra_guns.common.entity.bullet;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.lib.util.VectorUtils;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BaseBulletEntity extends AbstractHurtingProjectile {
    private BaseBullet bullet = TGItems.MUSKET_BULLET.get();
    private float damage;
    private float knockback;
    private int penetrate;
    private final List<Vec3> trails = new ArrayList<>();
    private Vec3 posO = Vec3.ZERO;
    private int life;

    public BaseBulletEntity(EntityType<? extends BaseBulletEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BaseBulletEntity(LivingEntity owner) {
        super(TGEntities.BASE_BULLET_ENTITY.get(), owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), owner.level());
        setOwner(owner);
    }

    public void setBullet(BaseBullet bullet) {
        this.bullet = bullet;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setKnockback(float knockback) {
        this.knockback = knockback;
    }

    public void setPenetrate(int penetrate) {
        this.penetrate = penetrate;
    }

    @Override
    public void tick() {
        NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Pre(this, bullet));
        super.tick();
        this.life++;
        if (disToOwner() > 128) this.discard();
        getOwner().sendSystemMessage(Component.literal(String.valueOf(getOwner().position().distanceTo(this.position()))));
        bullet.tick(this);
        savePos();

        NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Post(this, bullet));
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected @Nullable ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    public double disToOwner(){
        return this.position().distanceTo(getOwner().position());
    }

    private void savePos() {
        if (this.level().isClientSide) {
            if (trails.isEmpty()) {
                trails.add(this.position());
            }
            if (this.life % 4 == 0) {
                if (trails.get(trails.size() - 1).distanceTo(this.position()) > 1) {
                    trails.add(this.position());
                }
            }
            if (trails.size() > 10 || posO == this.position()) {
                trails.remove(0);
            }
            posO = this.position();
        }
    }


    public List<Vec3> getTrails() {
        return trails;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        BulletEvent.HitEvent.Entity hitEntityEvent = new BulletEvent.HitEvent.Entity(this, bullet, result);
        if (hitEntityEvent.isCanceled()) return;

        Entity hit = result.getEntity();
        Entity shooter = this.getOwner();

        if (hit instanceof LivingEntity target && !hit.is(shooter)) {
            BulletEvent.DamageEntityEvent damageEntityEvent = new BulletEvent.DamageEntityEvent(this, bullet, shooter, target);
            NeoForge.EVENT_BUS.post(damageEntityEvent);

            bullet.hitEffect(shooter, target, damage);
            if (knockback > 0) {
                BulletEvent.KnockbackEvent knockbackEvent = new BulletEvent.KnockbackEvent(this, bullet, knockback/8, 0f);
                NeoForge.EVENT_BUS.post(knockbackEvent);

                VectorUtils.knockBackA2B(this, target, knockbackEvent.getScale(), knockbackEvent.getMotionY());
            }

            BulletEvent.PenetrateEvent penetrateEvent = new BulletEvent.PenetrateEvent(this, bullet, penetrate);
            NeoForge.EVENT_BUS.post(penetrateEvent);
            int penetrate = penetrateEvent.getPenetrate();

            if (penetrate > 0) {
                this.penetrate--;
                if (this.penetrate==0) this.discard();
            } else if (penetrate != -1) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        BulletEvent.HitEvent.Block hitBlockEvent = new BulletEvent.HitEvent.Block(this, bullet, result);
        if (hitBlockEvent.isCanceled()) return;

        super.onHitBlock(result);
        this.discard();
    }
}
