package org.confluence.terra_guns.common.entity.bullet;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
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
    private static final EntityDataAccessor<String> COLOR_ID = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.STRING);
    private final BaseBullet bullet;
    public float damage;
    public float knockback;
    public int penetrate;
    public int hitBlockTime;
    private final List<Vec3> trails = new ArrayList<>();
    private String colorID;

    public BaseBulletEntity(EntityType<? extends BaseBulletEntity> entityType, Level level) {
        super(entityType, level);
        this.bullet = TGItems.MUSKET_BULLET.get();
    }

    public BaseBulletEntity(LivingEntity owner, BaseBullet bullet) {
        super(TGEntities.BASE_BULLET_ENTITY.get(), owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), owner.level());
        setOwner(owner);
        this.bullet = bullet;
    }

    public String getColorID() {
        if (!this.entityData.get(COLOR_ID).isEmpty()){
            return this.entityData.get(COLOR_ID);
        } else if (!bullet.colorID().isEmpty()){
            return bullet.colorID();
        }
        return BuiltInRegistries.ITEM.getKey(bullet).getPath();
    }

    public void setColorID(String colorID) {
        this.entityData.set(COLOR_ID, colorID);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(COLOR_ID, "");
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("ColorID", CompoundTag.TAG_STRING)) {
            this.setColorID(compound.getString("ColorID"));
        }
        if (compound.contains("Damage", CompoundTag.TAG_FLOAT)) {
            this.damage = compound.getFloat("Damage");
        }
        if (compound.contains("Knockback", CompoundTag.TAG_FLOAT)) {
            this.knockback = compound.getFloat("Knockback");
        }
        if (compound.contains("Penetrate", CompoundTag.TAG_INT)) {
            this.penetrate = compound.getInt("Penetrate");
        }
        if (compound.contains("HitBlockTime", CompoundTag.TAG_INT)) {
            this.hitBlockTime = compound.getInt("HitBlockTime");
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putString("ColorID", this.getColorID());
        compound.putFloat("Damage", this.damage);
        compound.putFloat("Knockback", this.knockback);
        compound.putInt("Penetrate", this.penetrate);
        compound.putInt("HitBlockTime", this.hitBlockTime);
    }

    @Override
    public void tick() {
        NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Pre(this, bullet));
        super.tick();
        if (disToOwner() > 128) this.discard();
        bullet.tick(this);
        this.saveTrailPos();

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
        if (getOwner()==null) return 256;
        return this.position().distanceTo(getOwner().position());
    }

    private void saveTrailPos() {
        if (this.level().isClientSide) {
            Vec3 currentPos = this.position();

            if (trails.isEmpty()) {
                trails.addLast(currentPos);
            }

            Vec3 lastPos = trails.getLast();
            double dist = lastPos.distanceTo(currentPos);

            double spacing = 0.4;
            if (dist > spacing) {
                int steps = Mth.floor(dist / spacing);
                Vec3 delta = currentPos.subtract(lastPos).scale(1.0 / steps);
                for (int i = 1; i <= steps; i++) {
                    trails.addLast(lastPos.add(delta.scale(i)));
                }
            } else {
                trails.addLast(currentPos);
            }

            while (trails.size() > 20) {
                trails.removeFirst();
            }
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

            this.bullet.onHitEntity(this, result);
            if (this.knockback > 0) {
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
    protected void onHitBlock(@NotNull BlockHitResult result) {
        BulletEvent.HitEvent.Block hitBlockEvent = new BulletEvent.HitEvent.Block(this,  bullet, result);
        if (hitBlockEvent.isCanceled()) return;

        super.onHitBlock(result);
        this.bullet.onHitBlock(this, result);

        this.hitBlockTime++;
    }

    public int getHitBlockTime() {
        return this.hitBlockTime;
    }
}
