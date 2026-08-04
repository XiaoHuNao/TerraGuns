package org.confluence.terra_guns.common.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BallisticsResolverTest {
    @Test
    void gunAndAmmoStatsAreCombinedOnce() {
        GunStats gun = new GunStats(10f, 2f, 1f, 0.2f, 1, 0.4f);
        AmmoStats ammo = new AmmoStats(3f, 0.5f, 2f, 0.25f, 2);

        Ballistics result = BallisticsResolver.resolve(gun, ammo);

        assertEquals(13f, result.damage(), 0.0001f);
        assertEquals(5f, result.velocity(), 0.0001f);
        assertEquals(1.25f, result.knockback(), 0.0001f);
        assertEquals(3, result.penetrate());
        assertEquals(0.4f, result.inaccuracy(), 0.0001f);
    }

    @Test
    void infinitePenetrationWinsOverFiniteValues() {
        GunStats gun = new GunStats(10f, 2f, 1f, 0.2f, 2, 0f);
        AmmoStats ammo = new AmmoStats(0f, 0f, 1f, 0f, -1);

        assertEquals(-1, BallisticsResolver.resolve(gun, ammo).penetrate());
    }
}
