package org.confluence.terra_guns.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import org.confluence.terra_guns.common.item.gun.GunItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Horse.class)
public abstract class HorseMixin {
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void cancel(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.getItemInHand(hand).getItem() instanceof GunItem) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
