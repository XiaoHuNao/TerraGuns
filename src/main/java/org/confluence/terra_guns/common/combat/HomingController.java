package org.confluence.terra_guns.common.combat;

import net.minecraft.world.phys.Vec3;

/**
 * Direction-only homing math. Keeping the turn rate angular produces a stable
 * circular arc instead of the speed-dependent wobble caused by blending the
 * velocity components independently.
 */
public final class HomingController {
    private static final double EPSILON = 1.0E-10D;
    private static final Vec3 UP = new Vec3(0.0D, 1.0D, 0.0D);
    private static final Vec3 RIGHT = new Vec3(1.0D, 0.0D, 0.0D);

    private HomingController() {
    }

    /**
     * Rotates {@code velocity} toward {@code targetOffset} by at most
     * {@code maxTurnRadians}, preserving speed.
     */
    public static Vec3 rotateVelocityToward(Vec3 velocity, Vec3 targetOffset, double maxTurnRadians) {
        double speed = velocity.length();
        if (speed <= EPSILON || targetOffset.lengthSqr() <= EPSILON || maxTurnRadians <= 0.0D) {
            return velocity;
        }

        Vec3 currentDirection = velocity.scale(1.0D / speed);
        Vec3 desiredDirection = targetOffset.normalize();
        double dot = clamp(currentDirection.dot(desiredDirection), -1.0D, 1.0D);
        double angle = Math.acos(dot);
        if (angle <= maxTurnRadians) {
            return desiredDirection.scale(speed);
        }

        // This is the component of the desired direction in the turn plane.
        // It remains stable for normal turns and gives the arc a constant
        // radius. Exactly opposite directions have no unique turn plane, so a
        // deterministic perpendicular is used instead of allowing a zero
        // vector to flip the projectile.
        Vec3 turnDirection = desiredDirection.subtract(currentDirection.scale(dot));
        if (turnDirection.lengthSqr() <= EPSILON) {
            Vec3 reference = Math.abs(currentDirection.y) < 0.9D ? UP : RIGHT;
            turnDirection = reference.subtract(currentDirection.scale(reference.dot(currentDirection)));
        }
        turnDirection = turnDirection.normalize();

        double turn = Math.min(Math.PI, maxTurnRadians);
        Vec3 steeredDirection = currentDirection.scale(Math.cos(turn))
                .add(turnDirection.scale(Math.sin(turn)))
                .normalize();
        return steeredDirection.scale(speed);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
