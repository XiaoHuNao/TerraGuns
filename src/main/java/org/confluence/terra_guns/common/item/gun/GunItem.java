package org.confluence.terra_guns.common.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.api.IAmmo;
import org.confluence.terra_guns.api.IGun;
import org.confluence.terra_guns.common.init.TGAttributes;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.init.TGSoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings({"unused", "unchecked"})
public abstract class GunItem<T extends Projectile> extends ProjectileWeaponItem implements IGun<T>{

    protected float damage = 1.0F;
    protected float weaponSpeed = 1.0F;
    protected int useDelay = 1;
    protected float knockBack = 1.0F;
    protected float crit = 0;
    protected float inaccuracy = 4.0F;

    public GunItem(Properties properties, float damage, float weaponSpeed, int useDelay, float knockBack, float crit, float inaccuracy) {
        super(properties);
        this.damage = damage;
        this.weaponSpeed = weaponSpeed;
        this.useDelay = useDelay;
        this.knockBack = knockBack;
        this.inaccuracy = Math.max(0, inaccuracy);
        this.crit = Math.max(0, crit);
    }
    public GunItem(Properties properties) {
        super(properties);
    }

    public GunItem(float damage, float weaponSpeed, int useDelay, float knockBack, float crit, float inaccuracy) {
        this(new Properties(), damage, weaponSpeed, useDelay, knockBack, crit, inaccuracy);
    }

    public GunItem(Properties properties, float damage, float weaponSpeed, int useDelay, float knockBack, float crit) {
        this(new Properties(), damage, weaponSpeed, useDelay, knockBack, crit, 0);
    }
    public GunItem(float damage, float weaponSpeed, int useDelay, float knockBack, float crit) {
        this(new Properties(), damage, weaponSpeed, useDelay, knockBack, crit, 0);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gun = player.getItemInHand(hand);
        ItemStack ammo = player.getProjectile(gun);

        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(gun);
        }
        if (!player.getAbilities().instabuild && ammo.isEmpty()) {
            return InteractionResultHolder.fail(gun);
        }
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    protected int getUseDelay(Player shooter, ItemStack gunStack, ItemStack ammoStack) {
        return useDelay;
    }

    @Override
    public final int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 0;
    }

    public UseAnim getUseAnimation(ItemStack gunStack) {
        return UseAnim.NONE;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack gunStack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            ItemStack ammoStack = player.getProjectile(gunStack);
            boolean infiniteAmmo = isAmmoInfinite(level, player, gunStack, ammoStack);

            if (infiniteAmmo || !ammoStack.isEmpty()) {
                if (ammoStack.isEmpty()) {
                    ammoStack = TGItems.MUSKET_BULLET.get().getDefaultInstance();
                }

                IGun<T> gun = (IGun<T>) gunStack.getItem();
                IAmmo<T> ammo = (IAmmo<T>) ammoStack.getItem();
                if (level.isClientSide) {
                    gun.clientShoot((ClientLevel) level, player, gunStack, ammoStack);
                    ammo.clientShoot((ClientLevel) level, player, gunStack, ammoStack);
                } else {
                    serverShoot((ServerLevel) level, player, gunStack, ammoStack, ammo, gun, infiniteAmmo);
                }
            }
        }
        return gunStack;
    }

    public float getRealAmmoSpeed(Player player, T projectile, ItemStack gunStack, ItemStack ammoStack) {
        float ammoSpeed = ((IAmmo<T>) ammoStack.getItem()).getAmmoSpeed(player, projectile, gunStack);
        float velocityMultiplier = ((IAmmo<T>) ammoStack.getItem()).getVelocityMultiplier(player, projectile, gunStack);
        return (weaponSpeed + ammoSpeed) * (getExtraUpdates(player, projectile, gunStack) + velocityMultiplier) / 10;
    }

    public float getExtraUpdates(Player player, T projectile, ItemStack gunStack) {
        return 0.0F;
    }

    public float getInaccuracy(Player player, T projectile, ItemStack gunStack, ItemStack ammoStack) {
        float ammoInaccuracy = ((IAmmo<T>) ammoStack.getItem()).getInaccuracy(player, projectile, gunStack);
        return this.inaccuracy + (inaccuracy * getInaccuracyMultiplier(player, projectile, gunStack)) + ammoInaccuracy;
    }


    public float getInaccuracyMultiplier(Player player, T projectile, ItemStack gunStack) {
        return 0.0F;
    }

    @Override
    public float getGunDamage(Player player, T projectile, ItemStack gunStack, ItemStack ammoStack) {
        IAmmo<T> ammo = (IAmmo<T>) ammoStack.getItem();
        float ammoDamage = ammo.getBaseDamage(player, projectile, gunStack) * ammo.getDamageMultiplier(player, projectile, gunStack);
        return this.damage + ammoDamage;
    }

    @Override
    public void serverShoot(ServerLevel level, Player player, ItemStack gunStack, ItemStack ammoStack, IAmmo<T> ammo, IGun<T> gun, boolean infiniteAmmo) {
        T projectile = ammo.createAmmo(level, player, gunStack, ammoStack);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getRealAmmoSpeed(player, projectile, gunStack, ammoStack), getInaccuracy(player, projectile, gunStack, ammoStack));

        ammo.beforeAmmoShoot(player, projectile, gunStack, ammoStack);
        if (level.addFreshEntity(projectile)) {
            gun.afterGunShoot(gunStack, player);
            if (!infiniteAmmo) {
                ammo.afterAmmoShoot(ammoStack, player);
            }
            player.getCooldowns().addCooldown(this, getUseDelay(player, gunStack, ammoStack));
        }
    }

    protected SoundEvent getShotSound() {
        return TGSoundEvents.SHOOT.get();
    }

    @Override
    public void clientShoot(ClientLevel level, Player shooter, ItemStack gunStack, ItemStack ammoStack) {
        level.playSound(shooter, shooter.getX(), shooter.getY(), shooter.getZ(), getShotSound(), SoundSource.PLAYERS, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 1.2F) * 0.5F);
    }

    @Override
    public boolean isAmmoInfinite(Level level, Player player, ItemStack gunStack, ItemStack ammoStack) {
        if (ammoStack.isEmpty()) return player.hasInfiniteMaterials();
        if (((IAmmo<T>) ammoStack.getItem()).isInfinite(player, ammoStack, gunStack)) return true;
        return !(player.getRandom().nextDouble() < player.getAttributeValue(TGAttributes.AMMO_CONSUME_CHANCE));
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return itemStack -> itemStack.getItem() instanceof IAmmo<?> ammo && ammo.isValidAmmo(itemStack);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float v, float v1, float v2, @Nullable LivingEntity livingEntity1) {}

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.empty().append(String.format("%.1f ", this.damage)).append(Component.translatable("terra_guns.attribute.weapon_damage")).withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.empty().append(String.format("%.1f ", this.weaponSpeed)).append(Component.translatable("terra_guns.attribute.weapon_speed")).withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.empty().append(String.valueOf(this.useDelay)).append(" ").append(Component.translatable("terra_guns.attribute.use_delay")).withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.empty().append(String.format("%.1f ", this.knockBack)).append(Component.translatable("terra_guns.attribute.knock_back")).withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.empty().append(String.format("%.1f", this.crit * 100)).append("% ").append(Component.translatable("terra_guns.attribute.crit")).append(" WIP").withColor(0x3f3f3f));
    }

    @Override
    public ItemStack getDefaultCreativeAmmo(@Nullable Player player, ItemStack projectileWeaponItem) {
        return TGItems.CHLOROPHYTE_BULLET.toStack();
    }
}
