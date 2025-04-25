package org.confluence.terra_guns.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;

public class AnimUtil {
    public static void stopAndPlayAnim(GeoItem geoItem, ItemStack itemStack, ServerPlayer serverPlayer, String controllerName, @Nullable String animName){
        long orAssignId = GeoItem.getOrAssignId(itemStack, serverPlayer.serverLevel());
        geoItem.getAnimatableInstanceCache().getManagerForId(orAssignId).stopTriggeredAnimation(controllerName, animName);
        geoItem.triggerAnim(serverPlayer, orAssignId, controllerName, animName);
    }
}
