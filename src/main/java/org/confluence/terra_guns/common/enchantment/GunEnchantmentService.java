package org.confluence.terra_guns.common.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.confluence.terra_guns.common.combat.GunFiringService;
import org.confluence.terra_guns.common.init.TGEnchantments;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.impl.BulletHandler;
import org.confluence.terra_guns.common.item.gun.BaseGun;

/** Server-side gameplay helpers for gun enchantments. */
public final class GunEnchantmentService {
    public static final int EMERGENCY_MELEE_COOLDOWN = 20 * 60;
    // 数组下标对应附魔等级，0 档代表没有附魔。每级数值可以单独调整。
    private static final int[] COMPRESSED_AMMO_USE_BY_LEVEL = {1, 2, 3, 4};
    private static final float[] COMPRESSED_DAMAGE_MULTIPLIER_BY_LEVEL = {1.0F, 1.2F, 1.4F, 1.6F};
    private static final int[] TEMPORARY_RESERVE_LEVEL_BY_LEVEL = {0, 1, 2, 3};
    private static final float[] TEMPORARY_RESERVE_CHANCE_BY_LEVEL = {0.0F, 0.02F, 0.03F, 0.04F};

    private GunEnchantmentService() {
    }

    public static int getLevel(Player player, ItemStack stack, ResourceKey<Enchantment> key) {
        Holder<Enchantment> holder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(key);
        return EnchantmentHelper.getTagEnchantmentLevel(holder, stack);
    }

    public static int getLevel(HolderLookup.Provider registries, ItemStack stack, ResourceKey<Enchantment> key) {
        Holder<Enchantment> holder = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
        return EnchantmentHelper.getTagEnchantmentLevel(holder, stack);
    }

    public static int getLevel(ItemStack stack, ResourceKey<Enchantment> key) {
        for (var entry : stack.getEnchantments().entrySet()) {
            if (entry.getKey().is(key)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }

    public static int getCompressedAmmoUse(Player player, ItemStack gunStack) {
        int level = getLevel(player, gunStack, TGEnchantments.COMPRESSED_TACTICS);
        return getConfiguredValue(COMPRESSED_AMMO_USE_BY_LEVEL, level);
    }

    public static float getCompressedDamageMultiplier(Player player, ItemStack gunStack) {
        int level = getLevel(player, gunStack, TGEnchantments.COMPRESSED_TACTICS);
        return getConfiguredValue(COMPRESSED_DAMAGE_MULTIPLIER_BY_LEVEL, level);
    }

    public static int getTemporaryReserveLevel(Player player, ItemStack gunStack) {
        int level = getLevel(player, gunStack, TGEnchantments.TEMPORARY_RESERVE);
        return getConfiguredValue(TEMPORARY_RESERVE_LEVEL_BY_LEVEL, level);
    }

    public static float getTemporaryReserveChance(int level) {
        return getConfiguredValue(TEMPORARY_RESERVE_CHANCE_BY_LEVEL, level);
    }

    public static int getEmergencyMeleeCooldownRemaining(ItemStack gunStack, Level level) {
        Long cooldownEnd = gunStack.get(TGDataComponents.EMERGENCY_MELEE_COOLDOWN_END.get());
        if (cooldownEnd == null) {
            return 0;
        }

        long remaining = cooldownEnd - level.getGameTime();
        return (int) Math.max(0L, Math.min(remaining, Integer.MAX_VALUE));
    }

    public static int getEmergencyMeleeCooldownRemaining(ServerPlayer player, ItemStack gunStack) {
        return getEmergencyMeleeCooldownRemaining(gunStack, player.serverLevel());
    }

    private static int getConfiguredValue(int[] values, int level) {
        int index = Math.max(0, Math.min(level, values.length - 1));
        return values[index];
    }

    private static float getConfiguredValue(float[] values, int level) {
        int index = Math.max(0, Math.min(level, values.length - 1));
        return values[index];
    }

    /**
     * Handles the emergency melee enchantment and cancels the normal player
     * attack whenever a nearby target is eligible for the gun's melee attack.
     */
    public static boolean tryEmergencyMelee(BaseGun gun, ItemStack gunStack, Player player, Entity target) {
        int level = Math.min(getLevel(player, gunStack, TGEnchantments.EMERGENCY_MELEE), 3);
        if (level <= 0 || !(target instanceof LivingEntity living)
                || !target.isAttackable()
                || target.skipAttackInteraction(player)) {
            return false;
        }

        double range = switch (level) {
            case 1 -> 4.0D;
            case 2 -> 3.0D;
            default -> 2.0D;
        };
        if (player.distanceToSqr(target) > range * range) {
            return false;
        }

        // Cancel the client-side vanilla attack as well. The server performs
        // the actual damage below and remains authoritative.
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return true;
        }

        if (isEmergencyMeleeOnCooldown(serverPlayer, gunStack)) {
            return true;
        }

        //TODO 紧急近战
        float damageMultiplier = switch (level) {
            case 1 -> 0.8F;
            case 2 -> 1.2F;
            default -> 1.5F;
        };
        if (living.hurt(player.damageSources().playerAttack(player), gun.getDefinition().damage() * damageMultiplier)) {
            gunStack.set(
                    TGDataComponents.EMERGENCY_MELEE_COOLDOWN_END.get(),
                    serverPlayer.serverLevel().getGameTime() + EMERGENCY_MELEE_COOLDOWN
            );
            serverPlayer.getInventory().setChanged();
            serverPlayer.containerMenu.broadcastChanges();

            // The radial burst uses the currently selected ammunition type,
            // but GunFiringService does not consume it for this special shot.
            ItemStack ammo = BulletHandler.getAmmo(serverPlayer, gunStack);
            if (!ammo.isEmpty()) {
                GunFiringService.fireRadial(serverPlayer, gun, gunStack, ammo, 8);
            }
        }
        return true;
    }

    private static boolean isEmergencyMeleeOnCooldown(ServerPlayer player, ItemStack gunStack) {
        return getEmergencyMeleeCooldownRemaining(player, gunStack) > 0;
    }
}
