package org.confluence.terra_guns.common.entity.bullet;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.common.init.TGEntities;
import org.jetbrains.annotations.NotNull;

public class BaseBulletEntity extends Projectile {
    public BaseBulletEntity(EntityType<? extends BaseBulletEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BaseBulletEntity(Level level) {
        super(TGEntities.BASE_BULLET_ENTITY.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {

    }
}
