package org.confluence.terra_guns.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.common.init.TGEntities;

public class SimpleTrailProjectile extends BaseAmmoEntity {
    private static final EntityDataAccessor<Integer> DATA_TRAIL_COLOR = SynchedEntityData.defineId(SimpleTrailProjectile.class, EntityDataSerializers.INT);

    public SimpleTrailProjectile(EntityType<SimpleTrailProjectile> type, Level level) {
        super(type, level);
    }

    public SimpleTrailProjectile(LivingEntity shooter, int trailColor) {
        super(TGEntities.SIMPLE_ITEM_MODEL_PROJECTILE.get(), shooter, Vec3.ZERO, shooter.level());
        setTrailColor(trailColor);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TRAIL_COLOR, 0xFFFFFF);
    }

    public void setTrailColor(int color) {
        entityData.set(DATA_TRAIL_COLOR, color);
    }

    public int getTrailColor() {
        return entityData.get(DATA_TRAIL_COLOR);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("TrailColor", getTrailColor());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setTrailColor(tag.getInt("TrailColor"));
    }
}
