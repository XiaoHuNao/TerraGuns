package org.confluence.terra_guns.common.component;

import com.google.common.collect.BiMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.TerraGuns;

// 反弹组件
public class BouncesComponent implements IHit {
    public static final ResourceLocation REGISTRY_NAME = TerraGuns.asResource("bounces");

    private int curBounces;
    private int maxBounces;

    public BouncesComponent(int maxBounces) {
        this.maxBounces = maxBounces;
    }

    @Override
    public void onHitBlock(Projectile projectile, BlockHitResult result) {
        if (projectile.isInWater() || this.curBounces >= this.maxBounces) {
            return;
        }
        bounce(projectile, result.getDirection());
        ++this.curBounces;
    }

    @Override
    public boolean hasConflict(BiMap<ResourceLocation, Pair<Integer,IHit>> map,Pair<Integer,IHit> hitPair) {
        Pair<Integer, IHit> integerIHitPair = map.get(PierceComponent.REGISTRY_NAME);
        if (integerIHitPair != null) {
            return hitPair.getFirst() >= integerIHitPair.getFirst();
        }
        return true;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return REGISTRY_NAME;
    }

    @Override
    public void onHitEntity(Projectile projectile, EntityHitResult result) {
        if (projectile.getType() == result.getEntity().getType()) {
            return;
        }
        if (projectile.isInWater() || this.curBounces >= this.maxBounces) {
            return;
        }
        bounce(projectile, projectile.getDirection());
        ++this.curBounces;
    }

    private void bounce(Projectile projectile, Direction direction) {
        Vec3 motion = projectile.getDeltaMovement();
        motion = switch (direction) {
            case DOWN, UP -> new Vec3(motion.x, -motion.y, motion.z);
            case NORTH, SOUTH -> new Vec3(motion.x, motion.y, -motion.z);
            case WEST, EAST -> new Vec3(-motion.x, motion.y, motion.z);
        };
        projectile.setDeltaMovement(motion);
        updateRotation(projectile, motion);
    }

    private void updateRotation(Projectile projectile, Vec3 motion) {
        float horizontalSpeed = (float) motion.horizontalDistance();
        projectile.setYRot((float) Math.toDegrees(Math.atan2(motion.x, motion.z)));
        projectile.setXRot((float) Math.toDegrees(Math.atan2(motion.y, horizontalSpeed)));
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
    }
}
