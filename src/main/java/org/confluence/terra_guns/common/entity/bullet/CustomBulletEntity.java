package org.confluence.terra_guns.common.entity.bullet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.common.init.TGEntities;
import org.jetbrains.annotations.NotNull;

public class CustomBulletEntity extends BaseBulletEntity implements ItemSupplier {
    protected float gravity = 0;

    public CustomBulletEntity(EntityType<? extends BaseBulletEntity> type, Level level) {
        super(type, level);
    }

    public CustomBulletEntity(LivingEntity owner, float gravity, ItemStack bullet) {
        this(TGEntities.GRAVITY_BULLET_ENTITY.get(), owner, gravity, bullet);
    }

    public CustomBulletEntity(EntityType<? extends BaseBulletEntity> type, LivingEntity owner, float gravity, ItemStack bullet) {
        super(type, owner, bullet);
        this.gravity = gravity;
    }

    public CustomBulletEntity(EntityType<? extends BaseBulletEntity> type, Level level, double x, double y, double z,
                              ItemStack bullet, float gravity) {
        super(type, level, x, y, z, bullet);
        this.gravity = gravity;
    }

    public float getBulletGravity() {
        return gravity;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Gravity", CompoundTag.TAG_FLOAT)) {
            this.gravity = compound.getFloat("Gravity");
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Gravity", this.gravity);
    }

    @Override
    protected void applyForces() {
        this.applyGravity();
    }

    @Override
    protected double getDefaultGravity() {
        return gravity;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return this.getBulletStack();
    }
}
