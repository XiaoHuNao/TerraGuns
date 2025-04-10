package org.confluence.terra_guns.item.bullet;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;

public class AbstractBullet extends Item {
    public AbstractBullet(Properties properties) {
        super(properties.component(DataComponents.MAX_STACK_SIZE, 1));
    }
}
