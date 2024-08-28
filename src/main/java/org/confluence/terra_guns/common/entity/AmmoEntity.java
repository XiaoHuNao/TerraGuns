package org.confluence.terra_guns.common.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.common.init.ModEntities;

public class AmmoEntity extends AbstractHurtingProjectile {
    private ItemStack ammoStack;

    private AmmoEntity(Level level, Player pShooter, double pOffsetX, double pOffsetY, double pOffsetZ) {
        super(ModEntities.AMMO.get(), pShooter, pOffsetX, pOffsetY, pOffsetZ, level);
    }

    public AmmoEntity(Level level, Player shooter, ItemStack ammoStack) {
        this(level, shooter, 0, 0, 0);
        setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.ammoStack = ammoStack;
    }

    public AmmoEntity(EntityType<AmmoEntity> type, Level level) {
        super(type,level);
    }

    @Override
    public void tick() {
        super.tick();
    }



//    @Override
//    protected void onHit(HitResult result) {
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult pResult) {
//        super.onHitBlock(pResult);
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult pResult) {
//        super.onHitEntity(pResult);
//    }
}
