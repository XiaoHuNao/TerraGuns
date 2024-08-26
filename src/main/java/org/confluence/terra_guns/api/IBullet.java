package org.confluence.terra_guns.api;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IBullet {
    void consume(ItemStack stack, Player shooter);

    Projectile createProjectile(Level world, Player shooter,ItemStack gun,ItemStack ammoStack);

    boolean isInfinite(Player player, ItemStack gunStack);

    boolean hasAmmo(ItemStack stack);

    float getProjectileSpeed(Player shooter, Projectile projectile, ItemStack gunStack);

    float getInaccuracy(Player shooter, Projectile projectile, ItemStack gunStack);

    float getBaseDamage();

    float getKnockBack();

    float getBonusDamage(Player player, Projectile projectile, ItemStack gunStack);

    float getDamageMultiplier(Player player, Projectile projectile, ItemStack gunStack);

    void setFinalDamage(float finalDamage);

    void modifyFinalProjectile(Projectile projectile, Player player, ItemStack gunStack);

    void clientShoot(ClientLevel level, Player player, ItemStack gunStack, ItemStack ammoStack);
}
