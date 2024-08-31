package org.confluence.terra_guns.common.item.gun;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class CustomGunItem extends GunItem {
    private Function<ItemStack, Integer> power;
    private Function<ItemStack, Integer> useDuration;

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration == null ? 0 : useDuration.apply(stack);
    }

    @Override
    public int getPower(ItemStack stack) {
        return power == null ? 0 : power.apply(stack);
    }

    public CustomGunItem setUseDuration(Function<ItemStack, Integer> useDuration) {
        this.useDuration = useDuration;
        return this;
    }

    public CustomGunItem setPower(Function<ItemStack, Integer> power) {
        this.power = power;
        return this;
    }
}
