package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.IAmmo;
import org.confluence.terra_guns.api.IGun;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;
import org.confluence.terra_guns.common.entity.SimpleTrailProjectile;

import java.util.List;

public class AmmoItem extends Item implements IAmmo<BaseAmmoEntity> {
    private final float baseDamage;
    private final float ammoVelocity;
    private final int velocityMultiplier;
    private final float knockBack;
    private final float inaccuracy;

    public AmmoItem(float baseDamage, float ammoVelocity, int velocityMultiplier, float knockBack, float inaccuracy) {
        super(new Properties().stacksTo(TerraGuns.IS_CONFLUENCE_LOADED ? 9999 : 99));
        this.baseDamage = baseDamage;
        this.ammoVelocity = ammoVelocity;
        this.velocityMultiplier = velocityMultiplier;
        this.knockBack = knockBack;
        this.inaccuracy = Math.max(0, inaccuracy);
    }
    public AmmoItem(Properties properties, float baseDamage, float ammoVelocity, int velocityMultiplier, float knockBack, float inaccuracy) {
        super(properties.stacksTo(TerraGuns.IS_CONFLUENCE_LOADED ? 9999 : 99));
        this.baseDamage = baseDamage;
        this.ammoVelocity = ammoVelocity;
        this.velocityMultiplier = velocityMultiplier;
        this.knockBack = knockBack;
        this.inaccuracy = Math.max(0, inaccuracy);
    }

    @Override
    public BaseAmmoEntity createAmmo(Level level, Player shooter, ItemStack gunStack, ItemStack ammoStack) {
        return new SimpleTrailProjectile(shooter, 0xFFFFFF);
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
    public float getVelocityMultiplier(Player shooter, BaseAmmoEntity projectile, ItemStack gunStack) {
        return velocityMultiplier;
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
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.empty().append(String.format("+%.1f ", this.baseDamage)).append(Component.translatable("terra_guns.attribute.weapon_damage")).withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.empty().append(String.format("+%.1f ", this.ammoVelocity)).append(Component.translatable("terra_guns.attribute.weapon_speed")).withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.empty().append(String.format("+%.1f ", this.knockBack)).append(Component.translatable("terra_guns.attribute.knock_back")).withStyle(ChatFormatting.BLUE));
    }
}
