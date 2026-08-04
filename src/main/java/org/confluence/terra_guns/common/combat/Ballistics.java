package org.confluence.terra_guns.common.combat;

public record Ballistics(
        float damage,
        float critical,
        float velocity,
        float knockback,
        int penetrate,
        float inaccuracy
) {
}
