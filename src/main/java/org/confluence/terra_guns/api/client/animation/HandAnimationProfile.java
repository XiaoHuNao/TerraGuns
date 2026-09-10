package org.confluence.terra_guns.api.client.animation;

import software.bernie.geckolib.animation.Animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class HandAnimationProfile {
    private final List<HandAnimationChannel> channels;

    private HandAnimationProfile(Builder builder) {
        if (builder.channels.isEmpty()) {
            throw new IllegalArgumentException("An animation profile needs at least one channel");
        }
        this.channels = List.copyOf(builder.channels);
    }

    public List<HandAnimationChannel> channels() {
        return channels;
    }

    public boolean isAnimation(HandAnimationAction action, String animationName) {
        if (animationName == null) {
            return false;
        }
        return channels.stream()
                .flatMap(channel -> channel.clip(action).stream())
                .anyMatch(clip -> clip.animation().equals(animationName));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static HandAnimationProfile legacy() {
        return builder()
                .channel(HandAnimationChannel.builder("gun")
                        .animation(HandAnimationAction.SHOOT, "fire")
                        .animation(HandAnimationAction.DRAW, "pick up")
                        .animation(HandAnimationAction.RELOAD, "reloading")
                        .build())
                .build();
    }

    public static HandAnimationProfile handgun() {
        return builder()
                .channel(HandAnimationChannel.builder("hand_pose")
                        .idle("idle")
                        .animation(HandAnimationAction.DRAW, "draw", Animation.LoopType.PLAY_ONCE)
                        .animation(HandAnimationAction.PUT_AWAY, "put_away", Animation.LoopType.PLAY_ONCE)
                        .animation(HandAnimationAction.INSPECT, "inspect", Animation.LoopType.PLAY_ONCE)
                        .build())
                .channel(HandAnimationChannel.builder("weapon_action")
                        .animation(HandAnimationAction.SHOOT, "shoot", Animation.LoopType.PLAY_ONCE)
                        .build())
                // The casing is a separate visual state.  Keeping it out of
                // the recoil controller prevents a new shot from rewinding a
                // casing that is still travelling through the ejection arc.
                // This channel intentionally has no idle animation: the
                // renderer hides the casing after the one-shot clip ends,
                // while inspect continues to own the Shell bone itself.
                .channel(HandAnimationChannel.builder("shell_action")
                        .animation(HandAnimationAction.EJECT_SHELL, "shell_eject", Animation.LoopType.PLAY_ONCE)
                        .build())
                .build();
    }

    public static final class Builder {
        private final List<HandAnimationChannel> channels = new ArrayList<>();

        public Builder channel(HandAnimationChannel channel) {
            HandAnimationChannel candidate = Objects.requireNonNull(channel, "channel");
            if (channels.stream().anyMatch(existing -> existing.name().equals(candidate.name()))) {
                throw new IllegalArgumentException("Duplicate animation channel: " + candidate.name());
            }
            channels.add(candidate);
            return this;
        }

        public HandAnimationProfile build() {
            return new HandAnimationProfile(this);
        }
    }
}
