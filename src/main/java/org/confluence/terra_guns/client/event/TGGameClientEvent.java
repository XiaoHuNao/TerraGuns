package org.confluence.terra_guns.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.client.init.TGKeys;
import org.confluence.terra_guns.common.init.TGGunSounds;
import org.confluence.terra_guns.common.init.TGTags;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletHandler;
import org.confluence.terra_guns.network.c2s.ShootPacketC2S;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortClientTickEvent;
import org.mesdag.portlib.event.client.PortInputEvent;

public class TGGameClientEvent {
    public static void init() {
        PortEventHandler.addListener(TGGameClientEvent::gunShot);
        PortEventHandler.addListener(TGGameClientEvent::cancelSwap);
    }

    private static void gunShot(PortClientTickEvent.PortPost event) {
        KeyMapping shoot = TGKeys.SHOOT.get();
        if (shoot.isDown()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null || player.isSpectator()) return;

            ItemStack mainHandItem = player.getMainHandItem();
            ItemCooldowns cooldowns = player.getCooldowns();
            if (mainHandItem.getItem() instanceof BaseGun baseGun && !cooldowns.isOnCooldown(baseGun)) {
                if (mainHandItem.is(TGTags.MANUAL_GUN) && !shoot.consumeClick()) return;

                GunEvent.UseGunEvent useGunEvent = new GunEvent.UseGunEvent(player, baseGun, baseGun.getCooldown());
                PortEventHandler.postEvent(useGunEvent);
                if (useGunEvent.isCanceled() || !BulletHandler.canShoot(player, mainHandItem))
                    return;

                player.playSound(TGGunSounds.getSound(mainHandItem), 1f, 1f);
                ShootPacketC2S.sendToServer();
                cooldowns.addCooldown(baseGun, useGunEvent.getCooldowns());
            }

        }
    }

    private static void cancelSwap(PortInputEvent.PortInteractionKeyMappingTriggered event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.getItemInHand(event.getHand()).getItem() instanceof BaseGun) {
            event.setSwingHand(false);
            if (event.isAttack()) event.setCanceled(true);
        }
    }
}
