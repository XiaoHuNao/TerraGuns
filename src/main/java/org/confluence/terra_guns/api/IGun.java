package org.confluence.terra_guns.api;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public interface IGun {
    void consume(ItemStack gunStack, Player player);

    void serverShoot(ServerLevel level, Player player, ItemStack gunStack, ItemStack ammoStack, IBullet bullet, IGun gun, boolean bulletFree);

    void clientShoot(ClientLevel level, Player player, ItemStack gunStack, ItemStack ammoStack);

    boolean shouldConsumeAmmo(Level level, Player player, ItemStack gunStack, ItemStack ammoStack);

}
