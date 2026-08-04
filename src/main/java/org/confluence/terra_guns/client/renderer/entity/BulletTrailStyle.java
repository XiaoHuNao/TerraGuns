package org.confluence.terra_guns.client.renderer.entity;

import net.minecraft.resources.ResourceLocation;

/**
 * Client-side presentation settings for one Terraria-style projectile trail.
 */
public record BulletTrailStyle(
        ResourceLocation trailTexture,
        ResourceLocation headTexture,
        float headWidth,
        float tailWidth,
        float headSize,
        float opacity,
        int maxPoints,
        boolean additive
) {
    public BulletTrailStyle {
        if (trailTexture == null || headTexture == null) {
            throw new IllegalArgumentException("trail textures are required");
        }
        if (!Float.isFinite(headWidth) || headWidth <= 0.0f) {
            throw new IllegalArgumentException("headWidth must be positive");
        }
        if (!Float.isFinite(tailWidth) || tailWidth < 0.0f || tailWidth > headWidth) {
            throw new IllegalArgumentException("tailWidth must be in [0, headWidth]");
        }
        if (!Float.isFinite(headSize) || headSize <= 0.0f) {
            throw new IllegalArgumentException("headSize must be positive");
        }
        if (!Float.isFinite(opacity) || opacity < 0.0f || opacity > 1.0f) {
            throw new IllegalArgumentException("opacity must be in [0, 1]");
        }
        if (maxPoints < 2) {
            throw new IllegalArgumentException("maxPoints must be at least 2");
        }
    }
}
