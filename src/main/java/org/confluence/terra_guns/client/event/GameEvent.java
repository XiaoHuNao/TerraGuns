package org.confluence.terra_guns.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.api.client.animation.HandAnimationAction;
import org.confluence.terra_guns.client.animation.GunCameraAnimation;
import org.confluence.terra_guns.client.init.TGKeys;
import org.confluence.terra_guns.client.renderer.entity.BulletVfxManager;
import org.confluence.terra_guns.common.init.TGGunSounds;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletHandler;
import org.confluence.terra_guns.network.c2s.InspectPacketC2S;
import org.confluence.terra_guns.network.c2s.ShootPacketC2S;
import software.bernie.geckolib.animatable.GeoItem;

@EventBusSubscriber(modid = TerraGuns.MODID, value = Dist.CLIENT)
public class GameEvent {
    private static final Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void gunShot(ClientTickEvent.Post event) {
        BulletVfxManager.tick();
        KeyMapping shoot = TGKeys.SHOOT.get();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            GunCameraAnimation.clear();
            return;
        }

        updateGunCameraAnimation(player);
        if (player.isSpectator() || !shoot.isDown()) return;

        ItemStack mainHandItem = player.getMainHandItem();
        ItemCooldowns cooldowns = player.getCooldowns();
        if (mainHandItem.getItem() instanceof BaseGun baseGun && !cooldowns.isOnCooldown(baseGun)) {
            if (BulletHandler.getAmmo(player, mainHandItem).isEmpty()) return;
            if (!baseGun.isAutomatic(mainHandItem) && !shoot.consumeClick()) return;

            GunEvent.UseGunEvent useGunEvent = new GunEvent.UseGunEvent(player, baseGun, baseGun.getCooldown());
            if (NeoForge.EVENT_BUS.post(useGunEvent).isCanceled()) return;

            player.playSound(TGGunSounds.getSound(mainHandItem), 1f, 1f);
            ShootPacketC2S.sendToServer();
            cooldowns.addCooldown(baseGun, Math.max(0, useGunEvent.getCooldowns()));
        }
    }

    private static void updateGunCameraAnimation(LocalPlayer player) {
        ItemStack mainHandItem = player.getMainHandItem();
        if (!(mainHandItem.getItem() instanceof BaseGun gun)) {
            GunCameraAnimation.clear();
            return;
        }

        long instanceId = GeoItem.getId(mainHandItem);
        boolean playing = gun.isAnimationPlaying(instanceId, HandAnimationAction.DRAW)
                || gun.isAnimationPlaying(instanceId, HandAnimationAction.PUT_AWAY)
                || gun.isAnimationPlaying(instanceId, HandAnimationAction.INSPECT)
                || gun.isAnimationPlaying(instanceId, HandAnimationAction.SHOOT);
        if (!playing) {
            GunCameraAnimation.clear();
        }
    }

    @SubscribeEvent
    public static void applyGunCamera(ViewportEvent.ComputeCameraAngles event) {
        GunCameraAnimation.apply(event);
    }

    @SubscribeEvent
    public static void inspectGun(ClientTickEvent.Post event) {
        LocalPlayer player = minecraft.player;
        if (player == null || player.isSpectator() || !TGKeys.INSPECT.get().consumeClick()) {
            return;
        }
        if (player.getMainHandItem().getItem() instanceof BaseGun) {
            InspectPacketC2S.sendToServer();
        }
    }

    @SubscribeEvent
    public static void bulletImpact(BulletEvent.ImpactEffectEvent event) {
        BulletVfxManager.play(event.getEffect(), event.getPosition());
    }

    @SubscribeEvent
    public static void renderBulletVfx(RenderLevelStageEvent event) {
        BulletVfxManager.render(event);
    }

    @SubscribeEvent
    public static void cancelSwap(InputEvent.InteractionKeyMappingTriggered event) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        if (player.getItemInHand(event.getHand()).getItem() instanceof BaseGun) {
            event.setSwingHand(false);
            if (event.isAttack()) event.setCanceled(true);
        }
    }
}
