package org.confluence.terra_guns.common.combat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletHandler;

/**
 * Server-authoritative entry point for every gun shot.
 */
public final class ShootingService {
    private ShootingService() {
    }

    public static boolean tryShoot(ServerPlayer player) {
        if (player.isSpectator()) {
            return false;
        }

        ItemStack gunStack = player.getMainHandItem();
        if (!(gunStack.getItem() instanceof BaseGun gun)) {
            return false;
        }

        if (player.getCooldowns().isOnCooldown(gun)) {
            return false;
        }

        GunEvent.UseGunEvent useEvent = new GunEvent.UseGunEvent(player, gun, gun.getCooldown());
        if (NeoForge.EVENT_BUS.post(useEvent).isCanceled()) {
            return false;
        }

        ItemStack ammo = BulletHandler.getAmmo(player, gunStack);
        GunEvent.GunFireEvent fireEvent = new GunEvent.GunFireEvent(player, gun, ammo, !ammo.isEmpty());
        NeoForge.EVENT_BUS.post(fireEvent);
        if (!fireEvent.isFire()) {
            return false;
        }

        ItemStack selectedAmmo = fireEvent.getAmmo();
        if (selectedAmmo == null || selectedAmmo.isEmpty()) {
            return false;
        }

        int projectileCount = GunFiringService.fire(player, gun, gunStack, selectedAmmo);
        if (projectileCount <= 0) {
            return false;
        }
        gun.fireAnimator(gunStack, player);
        consumeAmmo(player, gun, gunStack, selectedAmmo);

        int cooldown = Math.max(0, useEvent.getCooldowns());
        if (cooldown > 0) {
            player.getCooldowns().addCooldown(gun, cooldown);
        }
        return true;
    }

    private static void consumeAmmo(ServerPlayer player, BaseGun gun, ItemStack gunStack, ItemStack ammo) {
        boolean infinity = GunFiringService.isInfinite(ammo);

        GunEvent.ShrinkBulletEvent shrinkEvent = new GunEvent.ShrinkBulletEvent(player, gun, gunStack, ammo, infinity);
        NeoForge.EVENT_BUS.post(shrinkEvent);

        ItemStack bulletStack = shrinkEvent.getBulletStack();
        int shrink = Math.max(0, shrinkEvent.getShrink());
        if (!shrinkEvent.isInfinity() && !shrinkEvent.isCanceled() && bulletStack != null && shrink > 0) {
            bulletStack.shrink(shrink);
        }
    }
}
