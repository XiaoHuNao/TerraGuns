package org.confluence.terra_guns.common.entity.bullet;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseBulletEntity extends Projectile {
    private BaseBullet bullet;
    private float damage;
    private float knockback;
    private int penetrate;
    private final List<Vec3> trails = new ArrayList<>();
    private Vec3 posO = Vec3.ZERO;
    private int life;

    public BaseBulletEntity(EntityType<? extends BaseBulletEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BaseBulletEntity(Level level, BaseBullet bullet, float damage, float knockback, int penetrate) {
        super(TGEntities.BASE_BULLET_ENTITY.get(), level);
        this.bullet = bullet;
        this.damage = damage;
        this.knockback = knockback;
        this.penetrate = penetrate;
    }

    @Override
    public void tick() {
        NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Pre(this, bullet));
        super.tick();
        ++this.life;

        if (this.shouldDiscard()) {
            this.discard();
            return;
        }

        bullet.tick(this);
        savePos();

        HitResult hitResult = getHitResult();
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.hitTargetOrDeflectSelf(hitResult);
        }

        this.moveAndRotate();
        NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Post(this, bullet));
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

    private HitResult getHitResult() {
        Vec3 startVec = position();
        Vec3 endVec = startVec.add(getDeltaMovement());
        HitResult hitResult = this.level().clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0F), this::canHitEntity);
        if (entityHitResult != null) {
            hitResult = entityHitResult;
        }
        return hitResult;
    }

    private boolean shouldDiscard() {
        Entity owner = this.getOwner();
        boolean worldValid = this.level().hasChunkAt(this.blockPosition());

        return this.onGround()
                || this.level().isClientSide
                || bullet == null
                || (owner != null && owner.isRemoved())
                || !worldValid
                || penetrate == 0;
    }

    private void moveAndRotate() {
        //TODO 速度问题，疑似过快
        Vec3 motion = getDeltaMovement();
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;
        setPos(x, y, z);
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
                //TODO 枪械击退
//                if (target instanceof LivingEntity livingEntity){
//                    livingEntity.knockback(0.1,
//                            Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)),
//                            -Mth.cos(this.getYRot() * (float) (Math.PI / 180.0))
//                    );
//                }
//                Vec3 motion = this.getDeltaMovement().normalize();

//                Vec3 knockVec = new Vec3(motion.x, 0, motion.z).normalize().scale(knockback * 0.5);

//                target.push(knockVec.x, 0.1D, knockVec.z);
                Vec3 knockVec = this.getDeltaMovement().multiply(1, 0, 1).normalize().scale(knockback * 0.1);
                Vec3 totalKnock = new Vec3(knockVec.x, 0.0, knockVec.z);
                BulletEvent.KnockbackEvent knockbackEvent = new BulletEvent.KnockbackEvent(this, bullet, totalKnock);
                NeoForge.EVENT_BUS.post(knockbackEvent);

                target.push(knockbackEvent.getPenetrate());
            }

            BulletEvent.PenetrateEvent penetrateEvent = new BulletEvent.PenetrateEvent(this, bullet, penetrate);
            NeoForge.EVENT_BUS.post(penetrateEvent);
            int penetrate = penetrateEvent.getPenetrate();

            if (penetrate > 0) {
                this.penetrate--;
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
        setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }
}
