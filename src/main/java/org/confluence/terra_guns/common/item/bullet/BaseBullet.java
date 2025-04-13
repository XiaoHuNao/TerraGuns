package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

public class BaseBullet extends Item {
    public BaseBullet(Properties properties) {
        super(properties.stacksTo(99));
    }

    public void hitEffect(Entity entity) {

    }
}
