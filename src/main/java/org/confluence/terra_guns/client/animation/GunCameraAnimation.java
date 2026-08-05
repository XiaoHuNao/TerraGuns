package org.confluence.terra_guns.client.animation;

import net.neoforged.neoforge.client.event.ViewportEvent;
import software.bernie.geckolib.cache.object.GeoBone;

public final class GunCameraAnimation {
    private static final float EPSILON = 0.0001F;

    private static float pitch;
    private static float yaw;
    private static float roll;

    private GunCameraAnimation() {
    }

    public static void capture(GeoBone cameraBone) {
        if (cameraBone == null) {
            clear();
            return;
        }

        pitch = (float) Math.toDegrees(cameraBone.getRotX());
        yaw = (float) Math.toDegrees(cameraBone.getRotY());
        roll = (float) Math.toDegrees(cameraBone.getRotZ());
    }

    public static void clear() {
        pitch = 0;
        yaw = 0;
        roll = 0;
    }

    public static void apply(ViewportEvent.ComputeCameraAngles event) {
        if (Math.abs(pitch) < EPSILON && Math.abs(yaw) < EPSILON && Math.abs(roll) < EPSILON) {
            return;
        }
        if (event.getCamera().isDetached()) {
            return;
        }

        event.setPitch(event.getPitch() + pitch);
        event.setYaw(event.getYaw() + yaw);
        event.setRoll(event.getRoll() + roll);
    }
}
