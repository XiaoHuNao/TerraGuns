package org.confluence.terra_guns.common.component;

import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;


public class PierceComponent implements IHit {
    public static final ResourceLocation REGISTRY_NAME = TerraGuns.asResource("pierce");

    private int curPierce;
    private int maxPierce;
    private boolean canPierceBlock = false;

    public PierceComponent(int maxPierce, boolean canPierceBlock) {
        this.maxPierce = maxPierce;
        this.curPierce = 0;
    }

    @Override
    public void onHitEntity(Projectile projectile, EntityHitResult pResult) {
        Entity attack = projectile.getOwner();
        Entity target = pResult.getEntity();
        if (projectile instanceof BaseAmmoEntity ammoEntity) {
            if (curPierce < maxPierce) {
                curPierce++;
            }

            if (curPierce >= maxPierce) {
                projectile.discard();
            }
        }
    }

    @Override
    public void onHitBlock(Projectile entity, BlockHitResult pResult) {
        BlockState blockState = entity.level().getBlockState(pResult.getBlockPos());
        if (canPierceBlock) {
            if (curPierce < maxPierce) {
                curPierce++;
            }

            if (curPierce >= maxPierce) {
                entity.discard();
            }
        }else {
            entity.discard();
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return REGISTRY_NAME;
    }

    @Override
    public boolean hasConflict(BiMap<ResourceLocation, Pair<Integer, IHit>> map, Pair<Integer, IHit> hitPair) {
        Pair<Integer, IHit> pair = map.get(BouncesComponent.REGISTRY_NAME);
        if (pair != null) {
            return false;
        }
        return true;
    }

    public PierceComponent setMaxPierce(int maxPierce) {
        this.maxPierce = maxPierce;
        return this;
    }

    public PierceComponent setCanPierceBlock(boolean canPierceBlock) {
        this.canPierceBlock = canPierceBlock;
        return this;
    }
}