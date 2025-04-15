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
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.jetbrains.annotations.NotNull;

public class BaseBulletEntity extends Projectile {
    private BaseBullet bullet;
    private float damage;
    private float knockback;
    private int penetrate;

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
        super.tick();

        if (this.shouldDiscard()) {
            this.discard();
            return;
        }

        bullet.tick(this);


        HitResult hitResult = getHitResult();
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.hitTargetOrDeflectSelf(hitResult);
        }

        this.moveAndRotate();
    }

    private HitResult getHitResult(){
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
        Vec3 motion = getDeltaMovement();
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;
        setPos(x, y, z);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hit = result.getEntity();
        Entity shooter = this.getOwner();

        if (hit instanceof LivingEntity target && !hit.is(shooter)) {
            bullet.hitEffect(shooter, target, damage);

            if (knockback > 0) {
                Vec3 knockVec = this.getDeltaMovement().multiply(1, 0, 1).normalize()
                        .scale(knockback * 0.6 * Math.max(0.0, 1.0 - knockback));
                target.push(knockVec.x, 0.1, knockVec.z);
            }

            if (penetrate > 0) {
                penetrate--;
            } else if (penetrate != -1) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    public void setRot(float yRot, float xRot) {
        this.setYRot(yRot % 360.0F);
        this.setXRot(xRot % 360.0F);
    }
}
