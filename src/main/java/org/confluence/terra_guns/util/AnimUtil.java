package org.confluence.terra_guns.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.TerraGuns;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;

public class AnimUtil {
    public static void stopAndPlayAnim(GeoItem geoItem, ItemStack itemStack, ServerPlayer serverPlayer, String controllerName, @Nullable String animName){
        try {
            long orAssignId = GeoItem.getOrAssignId(itemStack, serverPlayer.serverLevel());
            geoItem.getAnimatableInstanceCache().getManagerForId(orAssignId).stopTriggeredAnimation(controllerName, animName);
            geoItem.triggerAnim(serverPlayer, orAssignId, controllerName, animName);
        } catch (NullPointerException e) {
            TerraGuns.LOGGER.warn(e.getMessage());
        }
    }
}
