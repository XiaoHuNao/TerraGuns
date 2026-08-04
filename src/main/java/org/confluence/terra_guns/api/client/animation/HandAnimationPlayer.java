package org.confluence.terra_guns.api.client.animation;

import java.util.ArrayDeque;
import java.util.Objects;

/** Runtime state for one hand. The game client owns one player per HumanoidArm. */
public final class HandAnimationPlayer {
    private static final int MAX_QUEUE_SIZE = 16;
    private static final int DEFAULT_BLEND_TICKS = 2;

    private final ArrayDeque<HandAnimation> queue = new ArrayDeque<>();
    private HandAnimation current;
    private long startedAt;
    private HandPose blendFrom = HandPose.IDENTITY;
    private int blendTicks;

    public boolean play(HandAnimation animation, HandAnimationPlayMode mode, long clientTick) {
        return play(animation, mode, clientTick, DEFAULT_BLEND_TICKS);
    }

    public boolean play(HandAnimation animation, HandAnimationPlayMode mode, long clientTick, int blendTicks) {
        Objects.requireNonNull(animation, "animation");
        Objects.requireNonNull(mode, "mode");
        if (blendTicks < 0) {
            throw new IllegalArgumentException("Blend duration cannot be negative");
        }

        if (current == null) {
            start(animation, clientTick, HandPose.IDENTITY, blendTicks);
            return true;
        }

        switch (mode) {
            case IF_IDLE -> {
                return false;
            }
            case QUEUE -> {
                if (queue.size() >= MAX_QUEUE_SIZE) {
                    return false;
                }
                queue.addLast(animation);
                return true;
            }
            case INTERRUPT -> {
                if (animation.priority() < current.priority()) {
                    return false;
                }
            }
            case FORCE -> {
                // FORCE intentionally ignores priority.
            }
        }

        HandPose previousPose = sample(clientTick, 0.0F);
        queue.clear();
        start(animation, clientTick, previousPose, blendTicks);
        return true;
    }

    public void tick(long clientTick) {
        while (current != null && !current.loop() && elapsed(clientTick) >= current.durationTicks()) {
            HandAnimation next = queue.pollFirst();
            if (next == null) {
                current = null;
            } else {
                start(next, clientTick, HandPose.IDENTITY, 0);
            }
        }
    }

    public HandPose sample(long clientTick, float partialTick) {
        if (current == null) {
            return HandPose.IDENTITY;
        }
        float partial = Math.max(0.0F, Math.min(1.0F, partialTick));
        float elapsed = elapsed(clientTick) + partial;
        if (current.loop()) {
            elapsed %= current.durationTicks();
        }
        HandPose pose = current.sample(elapsed);
        if (blendTicks <= 0) {
            return pose;
        }
        float blendProgress = Math.min(1.0F, elapsed / blendTicks);
        return HandPose.lerp(blendFrom, pose, Easing.EASE_OUT.apply(blendProgress));
    }

    public void stop() {
        current = null;
        queue.clear();
        blendFrom = HandPose.IDENTITY;
        blendTicks = 0;
    }

    public boolean isPlaying() {
        return current != null;
    }

    public HandAnimation currentAnimation() {
        return current;
    }

    private void start(HandAnimation animation, long clientTick, HandPose blendFrom, int blendTicks) {
        current = animation;
        startedAt = clientTick;
        this.blendFrom = blendFrom;
        this.blendTicks = blendTicks;
    }

    private long elapsed(long clientTick) {
        return Math.max(0L, clientTick - startedAt);
    }
}
