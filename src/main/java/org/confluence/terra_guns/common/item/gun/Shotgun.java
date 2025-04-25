package org.confluence.terra_guns.common.item.gun;

import org.confluence.lib.common.component.ModRarity;

public class Shotgun extends BaseGun{
    public Shotgun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, int penetrate, ModRarity rarity) {
        super(properties, cooldown, damage, velocity, knockback, critical, penetrate, rarity);
    }

    public Shotgun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, ModRarity rarity) {
        super(properties, cooldown, damage, velocity, knockback, critical, rarity);
    }
}
