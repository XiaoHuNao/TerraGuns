package org.confluence.terra_guns.common.combat;

/**
 * Runtime-independent gun values used by the ballistics pipeline.
 */
public record GunStats(
        float damage,
        float velocity,
        float knockback,
        float critical,
        int penetrate,
        float inaccuracy
) {
}
