package org.confluence.terra_guns.client.renderer.entity;

import org.confluence.terra_guns.TerraGuns;

import java.util.Map;

/**
 * Visual language for the ammunition families in TerraGuns.
 *
 * <p>The textures are white masks, so the existing per-ammo trail colors can
 * tint them without requiring a separate PNG for every bullet.</p>
 */
public final class BulletTrailStyles {
    private static final BulletTrailStyle STANDARD = style("trace_01", "flare_01", 0.12f, 0.008f, 0.10f, 0.92f, 20, true);
    private static final BulletTrailStyle ENERGY = style("trace_05", "flare_01", 0.15f, 0.012f, 0.12f, 0.96f, 20, true);
    private static final BulletTrailStyle BEAM = style("trace_07", "flare_01", 0.18f, 0.018f, 0.14f, 1.0f, 20, true);
    private static final BulletTrailStyle SPARK = style("trace_05", "circle_05", 0.14f, 0.008f, 0.13f, 0.95f, 18, true);

    private static final Map<String, BulletTrailStyle> STYLES = Map.ofEntries(
            Map.entry("meteor_shot", style("trace_05", "circle_05", 0.13f, 0.006f, 0.11f, 0.95f, 18, true)),
            Map.entry("crystal_bullet", style("trace_07", "flare_01", 0.14f, 0.010f, 0.12f, 0.95f, 20, true)),
            Map.entry("cursed_bullet", SPARK),
            Map.entry("chlorophyte_bullet", style("trace_05", "flare_01", 0.23f, 0.026f, 0.07f, 0.90f, 256, true)),
            Map.entry("high_velocity_bullet", style("trace_07", "flare_01", 0.10f, 0.003f, 0.09f, 1.0f, 16, true)),
            Map.entry("ichor_bullet", style("trace_01", "flare_01", 0.14f, 0.006f, 0.11f, 0.95f, 20, true)),
            Map.entry("venom_bullet", SPARK),
            Map.entry("party_bullet", style("trace_01", "circle_05", 0.16f, 0.010f, 0.13f, 0.92f, 20, true)),
            Map.entry("nano_bullet", ENERGY),
            Map.entry("exploding_bullet", style("trace_05", "flare_01", 0.17f, 0.014f, 0.14f, 0.96f, 20, true)),
            Map.entry("golden_bullet", style("trace_01", "flare_01", 0.13f, 0.008f, 0.11f, 0.94f, 20, true)),
            Map.entry("luminite_bullet", BEAM)
    );

    private BulletTrailStyles() {
    }

    public static BulletTrailStyle get(String bulletId) {
        return STYLES.getOrDefault(bulletId, STANDARD);
    }

    private static BulletTrailStyle style(String trail, String head, float headWidth, float tailWidth,
                                          float headSize, float opacity, int maxPoints, boolean additive) {
        return new BulletTrailStyle(
                TerraGuns.asResource("textures/vfx/trails/" + trail + ".png"),
                TerraGuns.asResource("textures/vfx/heads/" + head + ".png"),
                headWidth,
                tailWidth,
                headSize,
                opacity,
                maxPoints,
                additive
        );
    }
}
