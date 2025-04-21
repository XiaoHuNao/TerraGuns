package org.confluence.terra_guns.common.item.gun;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BaseGun extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseGun(Properties properties) {
        super(properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<BaseGun> gun = new AnimationController<>(this, "gun", state -> PlayState.CONTINUE);
        gun.triggerableAnim("gun_fire", RawAnimation.begin().then("animation.model.new", Animation.LoopType.PLAY_ONCE));
        controllers.add(gun);
    }

    public void fireAnimator(ItemStack itemStack, ServerPlayer serverPlayer){
        this.triggerAnim(serverPlayer, GeoItem.getOrAssignId(itemStack, serverPlayer.serverLevel()), "gun", "gun_fire");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
