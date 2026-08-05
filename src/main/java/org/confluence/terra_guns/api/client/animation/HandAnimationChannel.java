package org.confluence.terra_guns.api.client.animation;

import software.bernie.geckolib.animation.Animation;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class HandAnimationChannel {
    private final String name;
    private final HandAnimationClip idle;
    private final Map<HandAnimationAction, HandAnimationClip> animations;

    private HandAnimationChannel(Builder builder) {
        this.name = builder.name;
        this.idle = builder.idle;
        this.animations = Map.copyOf(builder.animations);
    }

    public String name() {
        return name;
    }

    public Optional<HandAnimationClip> idle() {
        return Optional.ofNullable(idle);
    }

    public Map<HandAnimationAction, HandAnimationClip> animations() {
        return animations;
    }

    public Optional<HandAnimationClip> clip(HandAnimationAction action) {
        return Optional.ofNullable(animations.get(Objects.requireNonNull(action, "action")));
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private final EnumMap<HandAnimationAction, HandAnimationClip> animations = new EnumMap<>(HandAnimationAction.class);
        private HandAnimationClip idle;

        private Builder(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Animation channel name cannot be blank");
            }
            this.name = name;
        }

        public Builder idle(String animation) {
            return idle(HandAnimationClip.loop(animation));
        }

        public Builder idle(HandAnimationClip animation) {
            this.idle = Objects.requireNonNull(animation, "animation");
            return this;
        }

        public Builder animation(HandAnimationAction action, String animation) {
            return animation(action, new HandAnimationClip(animation, Animation.LoopType.DEFAULT));
        }

        public Builder animation(HandAnimationAction action, String animation, Animation.LoopType loopType) {
            return animation(action, new HandAnimationClip(animation, loopType));
        }

        public Builder animation(HandAnimationAction action, HandAnimationClip animation) {
            action = Objects.requireNonNull(action, "action");
            if (action == HandAnimationAction.IDLE) {
                throw new IllegalArgumentException("IDLE must be configured with idle()");
            }
            animations.put(action, Objects.requireNonNull(animation, "animation"));
            return this;
        }

        public HandAnimationChannel build() {
            return new HandAnimationChannel(this);
        }
    }
}
