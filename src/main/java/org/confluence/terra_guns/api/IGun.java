package org.confluence.terra_guns.api;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IGun<T extends Projectile> {
    default void afterGunShoot(ItemStack gunStack, Player player) {
        gunStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
    }

    void serverShoot(ServerLevel level, Player player, ItemStack gunStack, ItemStack ammoStack, IAmmo<T> bullet, IGun<T> gun, boolean infiniteAmmo);

    void clientShoot(ClientLevel level, Player player, ItemStack gunStack, ItemStack ammoStack);

    boolean isAmmoInfinite(Level level, Player player, ItemStack gunStack, ItemStack ammoStack);

    float getGunDamage(Player player, T projectile, ItemStack gunStack, ItemStack ammoStack);
}
