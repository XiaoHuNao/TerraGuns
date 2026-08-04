package org.confluence.terra_guns.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
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
import org.confluence.terra_guns.api.client.animation.HandAnimationApi;
import org.confluence.terra_guns.api.client.animation.HandAnimationPlayMode;
import org.confluence.terra_guns.client.animation.TGHandAnimations;
import org.confluence.terra_guns.client.init.TGKeys;
import org.confluence.terra_guns.client.renderer.entity.BulletVfxManager;
import org.confluence.terra_guns.common.init.TGGunSounds;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletHandler;
import org.confluence.terra_guns.network.c2s.ShootPacketC2S;

@EventBusSubscriber(modid = TerraGuns.MODID, value = Dist.CLIENT)
public class GameEvent {
    private static final Minecraft minecraft = Minecraft.getInstance();

    static {
        TGHandAnimations.register();
    }

    @SubscribeEvent
    public static void gunShot(ClientTickEvent.Post event) {
        HandAnimationApi.tick();
        BulletVfxManager.tick();
        KeyMapping shoot = TGKeys.SHOOT.get();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            HandAnimationApi.reset();
            return;
        }
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

    @SubscribeEvent
    public static void bulletImpact(BulletEvent.ImpactEffectEvent event) {
        BulletVfxManager.play(event.getEffect(), event.getPosition());
    }

    @SubscribeEvent
    public static void renderBulletVfx(RenderLevelStageEvent event) {
        BulletVfxManager.render(event);
    }

    @SubscribeEvent
    public static void shotConfirmed(GunEvent.ShotConfirmedEvent event) {
        if (event.getPlayer() instanceof LocalPlayer player && player == minecraft.player
                && player.getMainHandItem().getItem() instanceof BaseGun) {
            HandAnimationApi.play(TGHandAnimations.RECOIL, player.getMainArm(), HandAnimationPlayMode.INTERRUPT);
        }
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
