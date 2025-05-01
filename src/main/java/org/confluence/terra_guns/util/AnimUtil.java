package org.confluence.terra_guns.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationProcessor;

public class AnimUtil {
    public static void stopAndPlayAnim(GeoItem geoItem, ItemStack itemStack, ServerPlayer serverPlayer, String controllerName, @Nullable String animName){
        long orAssignId = GeoItem.getOrAssignId(itemStack, serverPlayer.serverLevel());
        AnimatableManager<GeoAnimatable> managerForId = geoItem.getAnimatableInstanceCache().getManagerForId(orAssignId);
        AnimationController<GeoAnimatable> gun = managerForId.getAnimationControllers().get(controllerName);
        AnimationProcessor.QueuedAnimation currentAnimation = gun.getCurrentAnimation();

        if (currentAnimation != null && currentAnimation.animation().name().equals(animName)){
            managerForId.stopTriggeredAnimation(controllerName, animName);
        }

        geoItem.triggerAnim(serverPlayer, orAssignId, controllerName, animName);
    }
}
