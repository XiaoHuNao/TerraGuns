package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.api.IBullet;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;
import org.confluence.terra_guns.common.entity.SimpleItemModelProjectile;

public class BulletItem extends Item implements IBullet {
    private float projectileSpeed;
    private float inaccuracy;
    private float baseDamage;
    private float finalDamage;
    private float knockBack;

    public BulletItem(float baseDamage, float projectileSpeed, float knockBack, float inaccuracy) {
        super(new Properties().stacksTo(999));
        this.projectileSpeed = projectileSpeed;
        this.inaccuracy = inaccuracy;
        this.baseDamage = baseDamage;
        this.knockBack = knockBack;
    }

    @Override
    public void consume(ItemStack stack, Player shooter) {
        stack.shrink(1);
        if (stack.isEmpty()) {
            shooter.getInventory().removeItem(stack);
        }
    }

    @Override
    public Projectile createProjectile(Level level, Player shooter,ItemStack gunStack,ItemStack ammoStack) {
        return new SimpleItemModelProjectile(level, shooter,ammoStack);
    }

    @Override
    public boolean isInfinite(Player player, ItemStack gunStack) {
        return false;
    }

    @Override
    public boolean hasAmmo(ItemStack stack) {
        return true;
    }

    @Override
    public float getProjectileSpeed(Player shooter, Projectile projectile, ItemStack gunStack) {
        return this.projectileSpeed;
    }

    @Override
    public float getInaccuracy(Player shooter, Projectile projectile, ItemStack gunStack) {
        return Math.max(0, this.inaccuracy);
    }

    @Override
    public float getBaseDamage() {
        return this.baseDamage;
    }
    @Override
    public float getKnockBack() {
        return this.knockBack;
    }

    @Override
    public float getBonusDamage(Player player, Projectile projectile, ItemStack gunStack) {
        return 0;
    }

    @Override
    public float getDamageMultiplier(Player player, Projectile projectile, ItemStack gunStack) {
        return 1;
    }

    @Override
    public void setFinalDamage(float finalDamage) {
        this.finalDamage = finalDamage;
    }

    @Override
    public float getFinalDamage() {
        return finalDamage;
    }
    @Override
    public float getProjectileSpeed() {
        return projectileSpeed;
    }
    @Override
    public float getInaccuracy() {
        return inaccuracy;
    }

    @Override
    public void modifyFinalProjectile(Projectile projectile, Player player, ItemStack gunStack) {

    }

    @Override
    public void clientShoot(ClientLevel level, Player player, ItemStack gunStack, ItemStack ammoStack) {

    }

    @Override
    public void doPostHurtEffects(BaseAmmoEntity baseAmmoEntity, Entity target) {

    }
}
