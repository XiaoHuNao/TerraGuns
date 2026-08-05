package org.confluence.terra_guns.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.api.client.animation.HandAnimationAction;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoItem;

/**
 * Let a gun finish its custom put-away animation before vanilla replaces the
 * renderer's cached hand stack. Vanilla normally lowers a changing item for
 * only a few ticks, which cuts longer custom holster clips off mid-animation.
 */
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow private ItemStack mainHandItem;
    @Shadow private float mainHandHeight;

    @Inject(method = "tick", at = @At("HEAD"))
    private void terraGuns$finishGunPutAway(CallbackInfo callbackInfo) {
        LocalPlayer player = minecraft.player;
        if (player == null || mainHandItem.isEmpty()
                || ItemStack.matches(mainHandItem, player.getMainHandItem())
                || !(mainHandItem.getItem() instanceof BaseGun gun)) {
            return;
        }

        long instanceId = GeoItem.getId(mainHandItem);
        if (gun.isAnimationPlaying(instanceId, HandAnimationAction.PUT_AWAY)) {
            // Keep the cached old stack above vanilla's replacement threshold.
            // The normal tick logic resumes as soon as put_away finishes.
            mainHandHeight = Math.max(mainHandHeight, 0.6F);
        }
    }
}
