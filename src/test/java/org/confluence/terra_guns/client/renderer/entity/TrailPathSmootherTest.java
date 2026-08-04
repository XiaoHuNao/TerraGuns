package org.confluence.terra_guns.client.renderer.entity;

import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrailPathSmootherTest {
    @Test
    void curveEndsAtInterpolatedProjectileHead() {
        Vec3 head = new Vec3(3.0D, 2.0D, 0.0D);

        List<Vec3> result = TrailPathSmoother.smooth(
                List.of(Vec3.ZERO, new Vec3(1.0D, 0.0D, 0.0D), new Vec3(2.0D, 1.0D, 0.0D)),
                head,
                16
        );

        assertEquals(Vec3.ZERO, result.getFirst());
        assertEquals(head, result.getLast());
        assertTrue(result.size() > 4);
    }

    @Test
    void straightHistoryRemainsStraightAndOrdered() {
        List<Vec3> result = TrailPathSmoother.smooth(
                List.of(Vec3.ZERO, new Vec3(1.0D, 0.0D, 0.0D), new Vec3(2.0D, 0.0D, 0.0D)),
                new Vec3(3.0D, 0.0D, 0.0D),
                16
        );

        double previousX = Double.NEGATIVE_INFINITY;
        for (Vec3 point : result) {
            assertEquals(0.0D, point.y, 1.0E-10D);
            assertEquals(0.0D, point.z, 1.0E-10D);
            assertTrue(point.x > previousX);
            previousX = point.x;
        }
    }

    @Test
    void duplicateHistoryPointsDoNotCreateDegenerateSegments() {
        List<Vec3> result = TrailPathSmoother.smooth(
                List.of(Vec3.ZERO, Vec3.ZERO, new Vec3(1.0D, 0.0D, 0.0D)),
                new Vec3(1.0D, 0.0D, 0.0D),
                16
        );

        for (int index = 1; index < result.size(); index++) {
            assertTrue(result.get(index - 1).distanceToSqr(result.get(index)) > 0.0D);
        }
    }
}
