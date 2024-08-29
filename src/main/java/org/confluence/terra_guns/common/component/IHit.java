package org.confluence.terra_guns.common.component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public interface IHit {
    default void onHitEntity(Projectile entity, EntityHitResult pResult){}

    default void onHitBlock(Projectile entity, BlockHitResult pResult){
        BlockState blockstate = entity.level().getBlockState(pResult.getBlockPos());
        blockstate.onProjectileHit(entity.level(), blockstate, pResult, entity);
    }
}
