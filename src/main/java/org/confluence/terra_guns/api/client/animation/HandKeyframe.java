package org.confluence.terra_guns.api.client.animation;

import java.util.Objects;

/** A normalized keyframe, where progress is in the inclusive range [0, 1]. */
public record HandKeyframe(float progress, HandPose pose, Easing easing) {
    public HandKeyframe {
        if (!Float.isFinite(progress) || progress < 0.0F || progress > 1.0F) {
            throw new IllegalArgumentException("Keyframe progress must be between 0 and 1");
        }
        Objects.requireNonNull(pose, "pose");
        Objects.requireNonNull(easing, "easing");
    }
}
