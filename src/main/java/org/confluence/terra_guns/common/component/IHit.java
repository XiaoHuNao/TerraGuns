package org.confluence.terra_guns.common.component;

import com.google.common.collect.BiMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
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

    default boolean hasConflict(BiMap<ResourceLocation, Pair<Integer,IHit>> map,Pair<Integer,IHit> hitPair){
        return true;
    }

    ResourceLocation getRegistryName();
}
