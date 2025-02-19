package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.IAmmo;
import org.confluence.terra_guns.api.IGun;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;
import org.confluence.terra_guns.common.entity.SimpleItemModelProjectile;

public class AmmoItem extends Item implements IAmmo<BaseAmmoEntity> {
    private final float baseDamage;
    private final float ammoVelocity;
    private final float knockBack;
    private final float inaccuracy;

    public AmmoItem(float baseDamage, float ammoVelocity, float knockBack, float inaccuracy) {
        super(new Properties().stacksTo(TerraGuns.IS_CONFLUENCE_LOADED ? 9999 : 99));
        this.baseDamage = baseDamage;
        this.ammoVelocity = ammoVelocity;
        this.knockBack = knockBack;
        this.inaccuracy = Math.max(0, inaccuracy);
    }

    @Override
    public BaseAmmoEntity createAmmo(Level level, Player shooter, ItemStack gunStack, ItemStack ammoStack) {
        return new SimpleItemModelProjectile(shooter, ammoStack);
    }

    @Override
    public boolean isInfinite(Player shooter, ItemStack ammoStack, ItemStack gunStack) {
        return false;
    }

    @Override
    public boolean isValidAmmo(ItemStack ammoStack) {
        return true;
    }

    @Override
    public float getAmmoSpeed(Player shooter, BaseAmmoEntity projectile, ItemStack gunStack) {
        return ammoVelocity;
    }

    @Override
    public float getInaccuracy(Player shooter, BaseAmmoEntity projectile, ItemStack gunStack) {
        return inaccuracy;
    }

    @Override
    public float getKnockBack() {
        return knockBack;
    }

    @Override
    public float getBaseDamage(Player shooter, BaseAmmoEntity projectile, ItemStack gunStack) {
        return baseDamage;
    }

    @Override
    public float getDamageMultiplier(Player shooter, BaseAmmoEntity projectile, ItemStack gunStack) {
        return 1;
    }

    @Override
    public void beforeAmmoShoot(Player shooter, BaseAmmoEntity projectile, ItemStack gunStack, ItemStack ammoStack) {
        float damage = ((IGun<BaseAmmoEntity>) gunStack.getItem()).getGunDamage(shooter, projectile, gunStack, ammoStack);
        projectile.damageAndKnockback(getFinalDamage(damage, shooter, projectile, gunStack, ammoStack), getKnockBack());
    }

    @Override
    public void clientShoot(ClientLevel level, Player shooter, ItemStack gunStack, ItemStack ammoStack) {}

    @Override
    public void doPostHurtEffects(BaseAmmoEntity projectile, Entity target) {}

    @Override
    public float getFinalDamage(float damage, Player shooter, BaseAmmoEntity projectile, ItemStack gunStack, ItemStack ammoStack) {
        return damage;
    }
}
