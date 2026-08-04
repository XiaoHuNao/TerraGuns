package org.confluence.terra_guns.api.client.animation;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * An immutable hand animation made from normalized keyframes.
 * The animation can be registered once and played on either first-person hand.
 */
public record HandAnimation(ResourceLocation id, int durationTicks, int priority, boolean loop,
                            List<HandKeyframe> keyframes) {
    public HandAnimation {
        Objects.requireNonNull(id, "id");
        if (durationTicks <= 0) {
            throw new IllegalArgumentException("Hand-animation duration must be positive");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("Hand-animation priority cannot be negative");
        }
        if (keyframes == null || keyframes.isEmpty()) {
            throw new IllegalArgumentException("A hand animation needs at least one keyframe");
        }

        List<HandKeyframe> sorted = new ArrayList<>(keyframes);
        sorted.sort(Comparator.comparingDouble(HandKeyframe::progress));
        if (sorted.getFirst().progress() != 0.0F || sorted.getLast().progress() != 1.0F) {
            throw new IllegalArgumentException("Hand animations must start at 0 and end at 1");
        }
        for (int index = 1; index < sorted.size(); index++) {
            if (sorted.get(index - 1).progress() == sorted.get(index).progress()) {
                throw new IllegalArgumentException("Hand-animation keyframes cannot share progress");
            }
        }
        keyframes = List.copyOf(sorted);
    }

    public HandPose sample(float elapsedTicks) {
        float progress = clamp(elapsedTicks / durationTicks, 0.0F, 1.0F);
        if (keyframes.size() == 1) {
            return keyframes.getFirst().pose();
        }

        for (int index = 1; index < keyframes.size(); index++) {
            HandKeyframe next = keyframes.get(index);
            if (progress <= next.progress()) {
                HandKeyframe previous = keyframes.get(index - 1);
                float segment = (progress - previous.progress()) / (next.progress() - previous.progress());
                return HandPose.lerp(previous.pose(), next.pose(), previous.easing().apply(segment));
            }
        }
        return keyframes.getLast().pose();
    }

    public static Builder builder(ResourceLocation id, int durationTicks) {
        return new Builder(id, durationTicks);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static final class Builder {
        private final ResourceLocation id;
        private final int durationTicks;
        private final List<HandKeyframe> keyframes = new ArrayList<>();
        private int priority;
        private boolean loop;

        private Builder(ResourceLocation id, int durationTicks) {
            this.id = id;
            this.durationTicks = durationTicks;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder loop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public Builder keyframe(float progress, HandPose pose) {
            return keyframe(progress, pose, Easing.SMOOTH_STEP);
        }

        public Builder keyframe(float progress, HandPose pose, Easing easing) {
            keyframes.add(new HandKeyframe(progress, pose, easing));
            return this;
        }

        public HandAnimation build() {
            return new HandAnimation(id, durationTicks, priority, loop, keyframes);
        }
    }
}
