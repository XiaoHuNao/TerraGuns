package org.confluence.terra_guns.common.component;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Arrays;
import java.util.List;

public class PierceComponent implements IHit {
    private int curPierce;
    private final int maxPierce;

    public PierceComponent(int maxPierce) {
        this.maxPierce = maxPierce;
        this.curPierce = 0;
    }

    @Override
    public void onHitEntity(Projectile projectile, EntityHitResult pResult) {

    }

}