package org.confluence.terra_guns.client.animation;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.client.animation.Easing;
import org.confluence.terra_guns.api.client.animation.HandAnimation;
import org.confluence.terra_guns.api.client.animation.HandAnimationApi;
import org.confluence.terra_guns.api.client.animation.HandPose;

/** Built-in hand poses used by the firearms shipped with TerraGuns. */
public final class TGHandAnimations {
    public static final ResourceLocation RECOIL_ID = TerraGuns.asResource("recoil");
    public static final ResourceLocation EQUIP_ID = TerraGuns.asResource("equip");
    public static final ResourceLocation RELOAD_ID = TerraGuns.asResource("reload");

    public static final HandAnimation RECOIL = HandAnimation.builder(RECOIL_ID, 5)
            .priority(10)
            .keyframe(0.0F, HandPose.IDENTITY, Easing.LINEAR)
            .keyframe(0.14F, HandPose.of(0.0D, -0.012D, 0.055D, -5.0D, -0.6D, 0.0D), Easing.EASE_OUT)
            .keyframe(0.38F, HandPose.of(0.0D, 0.004D, -0.010D, 2.0D, 0.2D, 0.0D), Easing.EASE_IN_OUT)
            .keyframe(0.68F, HandPose.of(0.0D, 0.0D, 0.0D, -0.8D, 0.0D, 0.0D), Easing.EASE_OUT)
            .keyframe(1.0F, HandPose.IDENTITY, Easing.LINEAR)
            .build();

    public static final HandAnimation EQUIP = HandAnimation.builder(EQUIP_ID, 8)
            .priority(5)
            .keyframe(0.0F, HandPose.of(0.0D, -0.18D, 0.10D, -18.0D, 0.0D, 0.0D), Easing.EASE_OUT)
            .keyframe(0.68F, HandPose.of(0.0D, 0.02D, -0.01D, 2.0D, 0.0D, 0.0D), Easing.EASE_IN_OUT)
            .keyframe(1.0F, HandPose.IDENTITY, Easing.EASE_IN)
            .build();

    public static final HandAnimation RELOAD = HandAnimation.builder(RELOAD_ID, 18)
            .priority(30)
            .keyframe(0.0F, HandPose.IDENTITY, Easing.LINEAR)
            .keyframe(0.22F, HandPose.of(-0.035D, -0.10D, 0.08D, -12.0D, 12.0D, 4.0D), Easing.EASE_OUT)
            .keyframe(0.58F, HandPose.of(-0.07D, -0.19D, 0.13D, -28.0D, 24.0D, 8.0D), Easing.EASE_IN_OUT)
            .keyframe(0.82F, HandPose.of(0.0D, 0.02D, -0.01D, 3.0D, 0.0D, 0.0D), Easing.EASE_OUT)
            .keyframe(1.0F, HandPose.IDENTITY, Easing.EASE_IN)
            .build();

    private TGHandAnimations() {
    }

    public static void register() {
        HandAnimationApi.register(RECOIL);
        HandAnimationApi.register(EQUIP);
        HandAnimationApi.register(RELOAD);
    }
}
