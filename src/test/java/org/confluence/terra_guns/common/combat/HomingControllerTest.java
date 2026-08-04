package org.confluence.terra_guns.common.combat;

import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomingControllerTest {
    @Test
    void preservesSpeedAndLimitsTurnAngle() {
        Vec3 velocity = new Vec3(4.0D, 0.0D, 0.0D);
        Vec3 target = new Vec3(0.0D, 10.0D, 0.0D);
        double maxTurn = Math.toRadians(14.0D);

        Vec3 result = HomingController.rotateVelocityToward(velocity, target, maxTurn);

        assertEquals(velocity.length(), result.length(), 1.0E-9D);
        assertEquals(maxTurn, angleBetween(velocity, result), 1.0E-9D);
        assertTrue(result.y > 0.0D);
    }

    @Test
    void snapsToTargetWhenAlreadyInsideTurnLimit() {
        Vec3 velocity = new Vec3(2.0D, 0.0D, 0.0D);
        Vec3 target = new Vec3(10.0D, 1.0D, 0.0D);

        Vec3 result = HomingController.rotateVelocityToward(velocity, target, Math.toRadians(14.0D));

        assertEquals(0.0D, angleBetween(result, target), 1.0E-9D);
        assertEquals(2.0D, result.length(), 1.0E-9D);
    }

    @Test
    void oppositeDirectionProducesDeterministicArcInsteadOfZeroVelocity() {
        Vec3 velocity = new Vec3(3.0D, 0.0D, 0.0D);

        Vec3 result = HomingController.rotateVelocityToward(
                velocity,
                new Vec3(-5.0D, 0.0D, 0.0D),
                Math.toRadians(14.0D)
        );

        assertEquals(3.0D, result.length(), 1.0E-9D);
        assertTrue(result.y > 0.0D);
        assertEquals(Math.toRadians(14.0D), angleBetween(velocity, result), 1.0E-9D);
    }

    private static double angleBetween(Vec3 first, Vec3 second) {
        double dot = first.normalize().dot(second.normalize());
        return Math.acos(Math.max(-1.0D, Math.min(1.0D, dot)));
    }
}
