package org.confluence.terra_guns.client.renderer.entity;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/** Builds a render-only quadratic Bezier curve from spatial trail samples. */
final class TrailPathSmoother {
    private static final int SUBDIVISIONS = 5;
    private static final double DUPLICATE_EPSILON_SQR = 1.0E-8D;

    private TrailPathSmoother() {
    }

    static List<Vec3> smooth(List<Vec3> history, Vec3 head, int maxControlPoints) {
        int historyLimit = Math.max(1, maxControlPoints - 1);
        int start = Math.max(0, history.size() - historyLimit);
        List<Vec3> controls = new ArrayList<>(Math.min(maxControlPoints, history.size() + 1));
        for (int index = start; index < history.size(); index++) {
            appendDistinct(controls, history.get(index));
        }
        // The newest history sample is the entity's current tick position,
        // while the renderer receives an interpolated position between the
        // previous and current tick. Appending that interpolated head after
        // the current sample creates a short backwards segment. That segment
        // makes the head/tail gradient look reversed and can cause a visible
        // kink at the end of the ribbon. Replace the newest sample so the
        // control points always end at the rendered projectile head.
        if (controls.isEmpty()) {
            controls.add(head);
        } else {
            controls.set(controls.size() - 1, head);
        }

        if (controls.size() < 2) {
            return controls;
        }

        List<Vec3> curve = quadraticBezierChain(controls);

        // Avoid leaving a sub-pixel gap between the smoothed ribbon and its
        // interpolated projectile head due to floating-point evaluation.
        curve.set(curve.size() - 1, head);
        return curve;
    }

    private static List<Vec3> quadraticBezierChain(List<Vec3> controls) {
        List<Vec3> curve = new ArrayList<>((controls.size() - 1) * SUBDIVISIONS + 1);
        curve.add(controls.getFirst());

        for (int index = 0; index < controls.size() - 1; index++) {
            Vec3 start = index == 0
                    ? controls.getFirst()
                    : midpoint(controls.get(index - 1), controls.get(index));
            Vec3 control = controls.get(index);
            Vec3 end = index + 1 == controls.size() - 1
                    ? controls.getLast()
                    : midpoint(controls.get(index), controls.get(index + 1));

            for (int sample = 1; sample <= SUBDIVISIONS; sample++) {
                double progress = sample / (double) SUBDIVISIONS;
                appendDistinct(curve, quadraticBezier(start, control, end, progress));
            }
        }
        return curve;
    }

    private static Vec3 quadraticBezier(Vec3 start, Vec3 control, Vec3 end, double t) {
        double inverse = 1.0D - t;
        double startWeight = inverse * inverse;
        double controlWeight = 2.0D * inverse * t;
        double endWeight = t * t;
        return new Vec3(
                start.x * startWeight + control.x * controlWeight + end.x * endWeight,
                start.y * startWeight + control.y * controlWeight + end.y * endWeight,
                start.z * startWeight + control.z * controlWeight + end.z * endWeight
        );
    }

    private static Vec3 midpoint(Vec3 first, Vec3 second) {
        return first.add(second).scale(0.5D);
    }

    private static void appendDistinct(List<Vec3> points, Vec3 point) {
        if (points.isEmpty() || points.getLast().distanceToSqr(point) > DUPLICATE_EPSILON_SQR) {
            points.add(point);
        }
    }
}
