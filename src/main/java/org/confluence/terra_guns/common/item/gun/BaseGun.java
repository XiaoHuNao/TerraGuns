package org.confluence.terra_guns.common.item.gun;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BaseGun extends Item {
    public BaseGun(Properties properties) {
        super(properties.stacksTo(1));
    }

    public void canShot(ItemStack bullet){};
}
