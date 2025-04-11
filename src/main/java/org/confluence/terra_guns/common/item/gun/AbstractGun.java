package org.confluence.terra_guns.common.item.gun;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractGun extends Item {
    public AbstractGun(Properties properties) {
        super(properties.stacksTo(1));
    }

    public abstract void canShot(ItemStack bullet);
}
