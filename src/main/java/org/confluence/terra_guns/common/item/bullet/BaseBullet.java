package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;
import org.confluence.terra_guns.common.init.TGDamageTypes;
import org.confluence.terra_guns.common.init.TGDataComponents;

public class BaseBullet extends Item {
    private BulletPropertyComponent component;
    public BaseBullet(Properties properties, int damage, float velocity, float velocityMultiplier, float knockback, ModRarity rarity, boolean infinity) {
        super(properties.stacksTo(99));

        this.component = new BulletPropertyComponent(damage, velocity, velocityMultiplier, knockback, rarity, infinity);
        this.components().getOrDefault(TGDataComponents.BULLET_PROPERTY_COMPONENT.get(), component);
    }

    public void hitEffect(Player player, Entity entity, float amount) {
        entity.hurt(TGDamageTypes.of(entity.level(), TGDamageTypes.BULLET_DAMAGE, player), amount);
    }
}
