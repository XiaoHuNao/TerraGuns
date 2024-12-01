package org.confluence.terra_guns.common.item.gun;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Function;

public class CustomGunItem extends GunItem implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }
}
