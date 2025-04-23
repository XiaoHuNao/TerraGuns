package org.confluence.terra_guns.client.event;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.client.init.TGKeys;
import org.confluence.terra_guns.impl.SoundsManager;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletManager;
import org.confluence.terra_guns.network.c2s.ShootPacketC2S;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameEvent {
    private static final Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void firstPersonHandRender(RenderHandEvent event) {
        float equipProgress = event.getEquipProgress();
    }

    @SubscribeEvent
    public static void firstPersonArmRender(RenderArmEvent event) {
        HumanoidArm arm = event.getArm();
    }

    @SubscribeEvent
    public static void cameraDis(CalculateDetachedCameraDistanceEvent event) {
        Camera camera = event.getCamera();
    }

    @SubscribeEvent
    public static void gunShot(ClientTickEvent.Post event) {
        if (TGKeys.SHOOT.get().consumeClick()) {
            LocalPlayer player = minecraft.player;
            ItemCooldowns cooldowns = player.getCooldowns();

            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof BaseGun baseGun && !cooldowns.isOnCooldown(baseGun)) {
                BulletManager bulletManager = new BulletManager(player);
                GunEvent.UseGunEvent useGunEvent = new GunEvent.UseGunEvent(player, baseGun, baseGun.getCooldown());
                if (useGunEvent.isCanceled() || !bulletManager.canShoot()) return;

                player.playSound(SoundsManager.getSound(player.getMainHandItem()), 1f, 1f);
                ShootPacketC2S.sendToServer();
                cooldowns.addCooldown(baseGun, useGunEvent.getCooldowns());
            }
        }
    }

    @SubscribeEvent
    public static void cancelSwap(InputEvent.InteractionKeyMappingTriggered event) {
        LocalPlayer player = minecraft.player;
        if (player.getItemInHand(event.getHand()).getItem() instanceof BaseGun) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }
}
