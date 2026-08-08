package org.confluence.terra_guns.common.entity.bullet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.lib.common.LibDamageTypes;
import org.confluence.lib.util.VectorUtils;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.*;

public class BaseBulletEntity extends Projectile {
    private static final int MAX_LIFETIME = 200;
    private static final double MAX_OWNER_DISTANCE = 256.0D;
    private static final double MAX_RENDER_DISTANCE = 256.0D;
    private static final int MAX_ENTITY_COLLISIONS_PER_TICK = 32;
    private static final int MAX_TRAIL_POINTS = 64;
    private static final int CHLOROPHYTE_TRAIL_POINTS = 256;
    private static final double COLLISION_EPSILON = 0.08D;
    private static final double ENTITY_SWEEP_MARGIN = 0.10D;
    private static final double TRAIL_POINT_SPACING = 0.25D;
    private static final double TRAIL_POINT_EPSILON = 1.0E-6D;
    private static final EntityDataAccessor<String> COLOR_ID = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<ItemStack> BULLET = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EFFECT_STATE = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IGNORE_BLOCK_COLLISION = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Vector3f> INITIAL_VELOCITY = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> HAS_INITIAL_VELOCITY = SynchedEntityData.defineId(BaseBulletEntity.class, EntityDataSerializers.BOOLEAN);
    public float damage;
    public float knockback;
    public int hitBlockTimes;
    public int penetrate;
    private final Set<UUID> hitEntityIds = new HashSet<>();
    private final List<Vec3> trails = new ArrayList<>();
    private boolean appliedInitialVelocity;
    public double accelerationPower;

    public BaseBulletEntity(EntityType<? extends BaseBulletEntity> entityType, Level level) {
        super(entityType, level);
        this.accelerationPower = 0.1;
    }

    public BaseBulletEntity(EntityType<? extends Projectile> entityType, Level level, double x, double y, double z, ItemStack bullet) {
        super(entityType, level);
        this.setPos(x, y, z);
        this.setBullet(bullet);
    }

    public BaseBulletEntity(Level level, double x, double y, double z, ItemStack bullet) {
        this(TGEntities.BASE_BULLET_ENTITY.get(), level, x, y, z, bullet);
    }

    public BaseBulletEntity(EntityType<? extends Projectile> entityType, LivingEntity owner, ItemStack bullet) {
        this(entityType, owner.level(), owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), bullet);
        setOwner(owner);
    }

    public BaseBulletEntity(LivingEntity owner, ItemStack bullet) {
        this(TGEntities.BASE_BULLET_ENTITY.get(), owner, bullet);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        // A bullet is only 0.1 blocks wide. Scaling the render distance from
        // that size makes it disappear after roughly 51 blocks even though
        // the server keeps simulating it. Use the projectile range instead so
        // a distant shot remains visible until its server-side range expires.
        return distance < MAX_RENDER_DISTANCE * MAX_RENDER_DISTANCE;
    }

    protected ClipContext.Block getClipType() {
        return ClipContext.Block.COLLIDER;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return !this.isInvulnerableTo(source);
    }

    public String getColorID() {
        if (!this.entityData.get(COLOR_ID).isEmpty()) {
            return this.entityData.get(COLOR_ID);
        } else if (!this.getBullet().colorID().isEmpty()) {
            return this.getBullet().colorID();
        }
        return BuiltInRegistries.ITEM.getKey(this.getBulletStack().getItem()).getPath();
    }

    public void setColorID(String colorID) {
        this.entityData.set(COLOR_ID, colorID);
    }

    public void setBullet(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.is(Items.AIR)) {
            this.getEntityData().set(BULLET, this.getDefaultItem());
        } else {
            this.getEntityData().set(BULLET, stack.copyWithCount(1));
        }
    }

    public ItemStack getBulletStack() {
        return this.getEntityData().get(BULLET);
    }

    public BaseBullet getBullet() {
        Item item = this.getEntityData().get(BULLET).getItem();
        if (item instanceof BaseBullet bullet) {
            return bullet;
        }
        return (BaseBullet) getDefaultItem().getItem();
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = Math.max(0.0F, damage);
    }

    public float getKnockback() {
        return knockback;
    }

    public void setKnockback(float knockback) {
        this.knockback = Math.max(0.0F, knockback);
    }

    public int getPenetrate() {
        return penetrate;
    }

    public void setPenetrate(int penetrate) {
        this.penetrate = penetrate;
    }

    /**
     * Sets the projectile velocity and synchronizes the unquantized value to
     * the client. Add-entity packets clamp velocity components to 3.9, which
     * is too slow for high-velocity Terraria ammunition.
     */
    public void setInitialVelocity(Vec3 velocity) {
        this.setDeltaMovement(velocity);
        // Projectile.shoot() marks the entity as having an impulse. That flag
        // makes ServerEntity send another vanilla motion packet, which has
        // the same 3.9-per-axis limit as the add-entity packet.
        this.hasImpulse = false;
        if (!this.level().isClientSide) {
            this.entityData.set(INITIAL_VELOCITY, new Vector3f(
                    (float) velocity.x,
                    (float) velocity.y,
                    (float) velocity.z
            ));
            this.entityData.set(HAS_INITIAL_VELOCITY, true);
        }
    }

    public int getEffectState() {
        return this.entityData.get(EFFECT_STATE);
    }

    public void setEffectState(int effectState) {
        this.entityData.set(EFFECT_STATE, Math.max(0, effectState));
    }

    public boolean ignoresBlockCollision() {
        return this.entityData.get(IGNORE_BLOCK_COLLISION);
    }

    public void setIgnoresBlockCollision(boolean ignoresBlockCollision) {
        this.entityData.set(IGNORE_BLOCK_COLLISION, ignoresBlockCollision);
    }

    public LivingEntity getHomingTarget() {
        int targetId = this.entityData.get(HOMING_TARGET_ID);
        if (targetId < 0) {
            return null;
        }
        Entity target = level().getEntity(targetId);
        return target instanceof LivingEntity living ? living : null;
    }

    public void setHomingTarget(LivingEntity target) {
        this.entityData.set(HOMING_TARGET_ID, target == null ? -1 : target.getId());
    }

    public void clearHomingTarget() {
        this.entityData.set(HOMING_TARGET_ID, -1);
    }

    public boolean canHitTarget(Entity target) {
        return canHitEntity(target);
    }

    @SuppressWarnings("unchecked")
    public BaseBulletEntity createChild(Vec3 velocity, float damageMultiplier, int effectState) {
        return createChild(velocity, damageMultiplier, effectState, Vec3.ZERO);
    }

    @SuppressWarnings("unchecked")
    public BaseBulletEntity createChild(Vec3 velocity, float damageMultiplier, int effectState, Vec3 spawnOffset) {
        EntityType<? extends BaseBulletEntity> type = (EntityType<? extends BaseBulletEntity>) this.getType();
        BaseBulletEntity child;
        if (this instanceof CustomBulletEntity customBullet) {
            child = new CustomBulletEntity(
                    type,
                    this.level(),
                    this.getX() + spawnOffset.x,
                    this.getY() + spawnOffset.y,
                    this.getZ() + spawnOffset.z,
                    this.getBulletStack(),
                    customBullet.getBulletGravity()
            );
        } else {
            child = new BaseBulletEntity(
                    type,
                    this.level(),
                    this.getX() + spawnOffset.x,
                    this.getY() + spawnOffset.y,
                    this.getZ() + spawnOffset.z,
                    this.getBulletStack()
            );
        }
        child.setOwner(this.getOwner());
        child.setColorID(this.getColorID());
        child.setDamage(this.damage * Math.max(0.0F, damageMultiplier));
        child.setKnockback(this.knockback);
        child.setPenetrate(this.penetrate);
        child.setEffectState(effectState);
        child.accelerationPower = this.accelerationPower;
        child.setInitialVelocity(velocity);
        return child;
    }

    public DamageSource getDamageSource() {
        return LibDamageTypes.of(level(), LibDamageTypes.GUN_BULLET, this, getOwner());
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();
        return new ClientboundAddEntityPacket(this, serverEntity, i);
    }

    @Override
    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        // The velocity in the add-entity packet is quantized and capped at
        // 3.9 per axis. The exact value arrives through synced entity data
        // and is applied before the first client tick.
        this.setDeltaMovement(Vec3.ZERO);
        this.appliedInitialVelocity = false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(COLOR_ID, "");
        builder.define(BULLET, this.getDefaultItem());
        builder.define(HOMING_TARGET_ID, -1);
        builder.define(EFFECT_STATE, 0);
        builder.define(IGNORE_BLOCK_COLLISION, false);
        builder.define(INITIAL_VELOCITY, new Vector3f());
        builder.define(HAS_INITIAL_VELOCITY, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("ColorID", CompoundTag.TAG_STRING)) {
            this.setColorID(compound.getString("ColorID"));
        }
        if (compound.contains("Item", 10)) {
            this.setBullet(ItemStack.parse(this.registryAccess(), compound.getCompound("Item")).orElse(this.getDefaultItem()));
        } else {
            this.setBullet(this.getDefaultItem());
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
        if (compound.contains("EffectState", CompoundTag.TAG_INT)) {
            this.setEffectState(compound.getInt("EffectState"));
        }
        if (compound.contains("IgnoreBlockCollision", CompoundTag.TAG_BYTE)) {
            this.setIgnoresBlockCollision(compound.getBoolean("IgnoreBlockCollision"));
        }
        if (compound.contains("HitBlockTime", CompoundTag.TAG_INT)) {
            this.hitBlockTimes = compound.getInt("HitBlockTime");
        }
        if (compound.contains("acceleration_power", CompoundTag.TAG_DOUBLE)) {
            this.accelerationPower = compound.getDouble("acceleration_power");
        }
        if (compound.contains("Lifetime", CompoundTag.TAG_INT)) {
            this.tickCount = compound.getInt("Lifetime");
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putString("ColorID", this.getColorID());
        if (!getBulletStack().isEmpty()) {
            compound.put("Item", this.getBulletStack().save(this.registryAccess()));
        }
        compound.putFloat("Damage", this.damage);
        compound.putFloat("Knockback", this.knockback);
        compound.putInt("Penetrate", this.penetrate);
        compound.putInt("EffectState", this.getEffectState());
        compound.putBoolean("IgnoreBlockCollision", this.ignoresBlockCollision());
        compound.putInt("HitBlockTime", this.hitBlockTimes);
        compound.putDouble("acceleration_power", this.accelerationPower);
        compound.putInt("Lifetime", this.tickCount);
    }

    protected ItemStack getDefaultItem() {
        return TGItems.EMPTY_BULLET.toStack();
    }

    @Override
    public void tick() {
        NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Pre(this, this.getBullet()));
        this.applyInitialVelocity();
        super.tick();

        if (shouldDiscard()) {
            this.discard();
            NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Post(this, this.getBullet()));
            return;
        }

        this.getBullet().getBehavior().tick(this);
        this.saveTrailPos();
        this.checkInsideBlocks();
        this.applyForces();

        // Sweep the complete movement segment with an explicit line test. A
        // fast projectile can cross several entities in one tick, so a
        // per-position check is not enough and stopping after the first
        // entity makes penetration appear to be broken.
        Vec3 velocity = this.getDeltaMovement();
        Vec3 movement = velocity;
        int entityCollisions = 0;
        while (!this.isRemoved() && movement.lengthSqr() > 1.0E-7D) {
            this.setDeltaMovement(movement);
            Vec3 segmentStart = this.position();
            Vec3 segmentEnd = segmentStart.add(movement);
            HitResult hitResult = findHitResult(segmentStart, segmentEnd);

            if (hitResult == null) {
                this.setPos(segmentEnd.x, segmentEnd.y, segmentEnd.z);
                break;
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                Vec3 hitPosition = hitResult.getLocation();
                // Move to the exact swept collision point before posting the
                // hit event so effects use the entity's actual position.
                this.setPos(hitPosition.x, hitPosition.y, hitPosition.z);
                this.onHitBlock((BlockHitResult) hitResult);
                if (this.isRemoved()) {
                    NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Post(this, this.getBullet()));
                    return;
                }
                // Block behaviors such as ricochet already place the projectile
                // on the safe side of the surface. Continue next tick so a
                // reflected projectile does not make a second turn immediately.
                NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Post(this, this.getBullet()));
                return;
            }

            if (hitResult.getType() != HitResult.Type.ENTITY) {
                this.setPos(segmentEnd.x, segmentEnd.y, segmentEnd.z);
                break;
            }

            Vec3 hitPosition = hitResult.getLocation();
            this.setPos(hitPosition.x, hitPosition.y, hitPosition.z);
            this.onHitEntity((EntityHitResult) hitResult);
            if (this.isRemoved()) {
                NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Post(this, this.getBullet()));
                return;
            }

            entityCollisions++;
            Vec3 remaining = segmentEnd.subtract(hitPosition);
            if (remaining.lengthSqr() <= 1.0E-7D || entityCollisions >= MAX_ENTITY_COLLISIONS_PER_TICK) {
                this.setPos(hitPosition.x, hitPosition.y, hitPosition.z);
                movement = Vec3.ZERO;
                break;
            }

            Vec3 continuation = remaining.normalize();
            double offset = Math.min(COLLISION_EPSILON, remaining.length() * 0.5D);
            this.setPos(
                    hitPosition.x + continuation.x * offset,
                    hitPosition.y + continuation.y * offset,
                    hitPosition.z + continuation.z * offset
            );
            movement = segmentEnd.subtract(this.position());
        }

        // The temporary movement above is only the unprocessed part of this
        // tick. Preserve the projectile's actual velocity before applying
        // inertia, otherwise the next tick would lose speed after a hit.
        this.setDeltaMovement(velocity);
        float inertia = this.getInertia();
        Vec3 acceleration = velocity.lengthSqr() > 1.0E-7D
                ? velocity.normalize().scale(this.accelerationPower)
                : Vec3.ZERO;
        this.setDeltaMovement(velocity.add(acceleration).scale(inertia));
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);

        NeoForge.EVENT_BUS.post(new BulletEvent.Tick.Post(this, this.getBullet()));
    }

    /**
     * Finds the first collision on the whole movement segment for this tick.
     * The block ray and entity sweep are evaluated independently, then the
     * closest result wins. This prevents a fast bullet from skipping an
     * entity between two sampled positions and also preserves block ordering.
     */
    private HitResult findHitResult(Vec3 start, Vec3 end) {
        EntityHitResult entityHit = findEntityHit(start, end);
        BlockHitResult blockHit = ignoresBlockCollision() ? null : findBlockHit(start, end);
        if (entityHit == null) return blockHit;
        if (blockHit == null) return entityHit;

        double entityDistance = start.distanceToSqr(entityHit.getLocation());
        double blockDistance = start.distanceToSqr(blockHit.getLocation());
        return entityDistance <= blockDistance ? entityHit : blockHit;
    }

    private BlockHitResult findBlockHit(Vec3 start, Vec3 end) {
        BlockHitResult result = this.level().clip(new ClipContext(
                start,
                end,
                this.getClipType(),
                ClipContext.Fluid.NONE,
                this
        ));
        return result.getType() == HitResult.Type.BLOCK ? result : null;
    }

    private EntityHitResult findEntityHit(Vec3 start, Vec3 end) {
        Vec3 movement = end.subtract(start);
        AABB searchBox = this.getBoundingBox().expandTowards(movement).inflate(ENTITY_SWEEP_MARGIN);
        List<Entity> candidates = this.level().getEntities(this, searchBox, this::canHitEntity);
        EntityHitResult closest = null;
        double closestDistance = Double.POSITIVE_INFINITY;
        double horizontalExtent = this.getBbWidth() * 0.5D;
        double verticalExtent = this.getBbHeight() * 0.5D;

        for (Entity candidate : candidates) {
            AABB collisionBox = candidate.getBoundingBox().inflate(horizontalExtent, verticalExtent, horizontalExtent);
            Optional<Vec3> hitPosition = collisionBox.clip(start, end);
            if (hitPosition.isEmpty()) {
                continue;
            }

            Vec3 location = hitPosition.get();
            double distance = start.distanceToSqr(location);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = new EntityHitResult(candidate, location);
            }
        }
        return closest;
    }

    private void applyInitialVelocity() {
        if (!this.level().isClientSide || this.appliedInitialVelocity || !this.entityData.get(HAS_INITIAL_VELOCITY)) {
            return;
        }

        Vector3f velocity = this.entityData.get(INITIAL_VELOCITY);
        this.setDeltaMovement(new Vec3(velocity.x(), velocity.y(), velocity.z()));
        this.appliedInitialVelocity = true;
    }

    private boolean shouldDiscard() {
        if (this.tickCount >= MAX_LIFETIME) {
            return true;
        }
        if (this.level().isClientSide) {
            return false;
        }

        Entity owner = this.getOwner();
        if (!this.level().hasChunkAt(this.blockPosition())) {
            return true;
        }
        if (owner == null) {
            return false;
        }
        if (owner.isRemoved() || disToOwner() > MAX_OWNER_DISTANCE) {
            return true;
        }

        Vec3 nextPosition = this.position().add(this.getDeltaMovement());
        return nextPosition.distanceTo(owner.position()) > MAX_OWNER_DISTANCE;
    }

    /**
     * Hook for projectile variants that need to apply gravity or other forces.
     */
    protected void applyForces() {
    }

    protected float getInertia() {
        return 0.95F;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    public double disToOwner() {
        if (getOwner() == null) return MAX_OWNER_DISTANCE;
        return this.position().distanceTo(getOwner().position());
    }

    private void saveTrailPos() {
        if (this.level().isClientSide) {
            Vec3 currentPos = this.position();

            if (trails.isEmpty()) {
                trails.addLast(currentPos);
                return;
            }

            Vec3 lastPos = trails.getLast();
            double dist = lastPos.distanceTo(currentPos);

            if (dist <= TRAIL_POINT_EPSILON) {
                return;
            }
            if (dist > TRAIL_POINT_SPACING) {
                int steps = (int) Math.ceil(dist / TRAIL_POINT_SPACING);
                Vec3 delta = currentPos.subtract(lastPos).scale(1.0 / steps);
                for (int i = 1; i <= steps; i++) {
                    trails.addLast(lastPos.add(delta.scale(i)));
                }
            } else {
                trails.addLast(currentPos);
            }

            int maxTrailPoints = "chlorophyte_bullet".equals(this.getColorID())
                    ? CHLOROPHYTE_TRAIL_POINTS
                    : MAX_TRAIL_POINTS;
            while (trails.size() > maxTrailPoints) {
                trails.removeFirst();
            }
        }
    }


    public List<Vec3> getTrails() {
        return trails;
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (!target.canBeHitByProjectile()) {
            return false;
        } else {
            Entity entity = this.getOwner();
            return target != entity
                    && !hitEntityIds.contains(target.getUUID())
                    && (entity == null || !entity.isPassengerOfSameVehicle(target));
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Entity hit = result.getEntity();
        // Mark the entity before posting events. If an event is canceled, the
        // projectile must still move past that entity instead of repeatedly
        // resolving the same collision in the penetration sweep.
        hitEntityIds.add(hit.getUUID());
        if (NeoForge.EVENT_BUS.post(new BulletEvent.HitEvent.Entity(this, this.getBullet(), result)).isCanceled())
            return;

        Entity shooter = this.getOwner();

        if (!level().isClientSide && hit != shooter && !this.isRemoved()) {
            BulletEvent.DamageEntityEvent damageEvent = new BulletEvent.DamageEntityEvent(this, this.getBullet(), shooter, hit);
            if (NeoForge.EVENT_BUS.post(damageEvent).isCanceled()) {
                return;
            }

            hit.hurt(this.getDamageSource(), this.getDamage());
            this.getBullet().getBehavior().onHitEntity(this, result);
            if (this.knockback > 0) {
                BulletEvent.KnockbackEvent knockbackEvent = new BulletEvent.KnockbackEvent(this, this.getBullet(), knockback / 8, 0f);
                NeoForge.EVENT_BUS.post(knockbackEvent);

                VectorUtils.knockBackA2B(this, hit, knockbackEvent.getScale(), knockbackEvent.getMotionY());
            }

            BulletEvent.PenetrateEvent penetrateEvent = new BulletEvent.PenetrateEvent(this, this.getBullet(), this.penetrate);
            NeoForge.EVENT_BUS.post(penetrateEvent);
            int remaining = penetrateEvent.getPenetrate();

            if (remaining < 0) {
                return;
            } else if (remaining <= 1) {
                this.discard();
                return;
            }
            this.penetrate = remaining - 1;
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (NeoForge.EVENT_BUS.post(new BulletEvent.HitEvent.Block(this, this.getBullet(), result)).isCanceled())
            return;

        super.onHitBlock(result);
        if (!this.getBullet().getBehavior().onHitBlock(this, result)) {
            this.discard();
        }

        this.hitBlockTimes++;
    }
}
