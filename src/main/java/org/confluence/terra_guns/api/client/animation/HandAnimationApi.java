package org.confluence.terra_guns.api.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Public first-person hand-animation API for TerraGuns and addon mods.
 * This class is client-only: register animations during client setup and play them from client events.
 */
public final class HandAnimationApi {
    private static final Map<ResourceLocation, HandAnimation> ANIMATIONS = new ConcurrentHashMap<>();
    private static final EnumMap<HumanoidArm, HandAnimationPlayer> PLAYERS = new EnumMap<>(HumanoidArm.class);
    private static long clientTick;

    static {
        for (HumanoidArm arm : HumanoidArm.values()) {
            PLAYERS.put(arm, new HandAnimationPlayer());
        }
    }

    private HandAnimationApi() {
    }

    public static void register(HandAnimation animation) {
        Objects.requireNonNull(animation, "animation");
        ANIMATIONS.put(animation.id(), animation);
    }

    public static Optional<HandAnimation> find(ResourceLocation id) {
        return Optional.ofNullable(ANIMATIONS.get(id));
    }

    public static boolean play(ResourceLocation id, HumanoidArm arm, HandAnimationPlayMode mode) {
        HandAnimation animation = ANIMATIONS.get(id);
        return animation != null && play(animation, arm, mode);
    }

    public static boolean play(ResourceLocation id, HumanoidArm arm, HandAnimationPlayMode mode, int blendTicks) {
        HandAnimation animation = ANIMATIONS.get(id);
        return animation != null && play(animation, arm, mode, blendTicks);
    }

    public static boolean play(HandAnimation animation, HumanoidArm arm, HandAnimationPlayMode mode) {
        return play(animation, arm, mode, 2);
    }

    public static boolean play(HandAnimation animation, HumanoidArm arm, HandAnimationPlayMode mode, int blendTicks) {
        Objects.requireNonNull(animation, "animation");
        Objects.requireNonNull(arm, "arm");
        Objects.requireNonNull(mode, "mode");
        register(animation);
        return PLAYERS.get(arm).play(animation, mode, clientTick, blendTicks);
    }

    public static void stop(HumanoidArm arm) {
        PLAYERS.get(Objects.requireNonNull(arm, "arm")).stop();
    }

    public static void stopAll() {
        PLAYERS.values().forEach(HandAnimationPlayer::stop);
    }

    public static boolean isPlaying(HumanoidArm arm) {
        return PLAYERS.get(Objects.requireNonNull(arm, "arm")).isPlaying();
    }

    public static Optional<HandAnimation> current(HumanoidArm arm) {
        return Optional.ofNullable(PLAYERS.get(Objects.requireNonNull(arm, "arm")).currentAnimation());
    }

    public static HandPose sample(HumanoidArm arm, float partialTick) {
        return PLAYERS.get(Objects.requireNonNull(arm, "arm")).sample(clientTick, partialTick);
    }

    public static void apply(PoseStack poseStack, HumanoidArm arm) {
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        sample(arm, partialTick).apply(poseStack, arm);
    }

    /** Call once from the client tick event. */
    public static void tick() {
        clientTick++;
        PLAYERS.values().forEach(player -> player.tick(clientTick));
    }

    /** Clears runtime state when the local player leaves a level or disconnects. */
    public static void reset() {
        clientTick = 0L;
        stopAll();
    }
}
