package org.confluence.terra_guns.api;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IAmmo<T extends Projectile> {
    T createAmmo(Level level, Player shooter, ItemStack gunStack, ItemStack ammoStack);

    void beforeAmmoShoot(Player shooter, T ammoEntity, ItemStack gunStack, ItemStack ammoStack);

    default void afterAmmoShoot(ItemStack ammoStack, Player shooter) {
        ammoStack.consume(1, shooter);
    }

    boolean isInfinite(Player shooter, ItemStack ammoStack, ItemStack gunStack);

    boolean isValidAmmo(ItemStack ammoStack);

    float getAmmoSpeed(Player shooter, T ammoEntity, ItemStack gunStack);
    float getVelocityMultiplier(Player shooter, T ammoEntity, ItemStack gunStack);

    float getInaccuracy(Player shooter, T ammoEntity, ItemStack gunStack);

    float getBaseDamage(Player shooter, T ammoEntity, ItemStack gunStack);

    float getDamageMultiplier(Player shooter, T ammoEntity, ItemStack gunStack);

    float getKnockBack();

    void clientShoot(ClientLevel level, Player shooter, ItemStack gunStack, ItemStack ammoStack);

    void doPostHurtEffects(T ammoEntity, Entity target);

    float getFinalDamage(float damage, Player shooter, T ammoEntity, ItemStack gunStack, ItemStack ammoStack);
}
