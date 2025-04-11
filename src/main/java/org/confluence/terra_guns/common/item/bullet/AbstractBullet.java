package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

public abstract class AbstractBullet extends Item {
    public AbstractBullet(Properties properties) {
        super(properties.stacksTo(99));
    }

    public abstract void hitEffect(Entity entity);
}
