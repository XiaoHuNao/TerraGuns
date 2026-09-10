package org.confluence.terra_guns.impl;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.combat.GunFiringService;
import org.confluence.terra_guns.common.enchantment.GunEnchantmentService;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.init.TGTags;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.ArrayList;
import java.util.List;

public class BulletHandler {
    /**
     * 获取玩家背包中第一个兼容该枪的子弹
     */
    public static ItemStack getAmmo(Player player, ItemStack gun) {
        return findAmmo(player, gun, 1);
    }

    /**
     * 获取本次射击所需数量足够的第一组兼容弹药。
     */
    public static ItemStack getAmmoForShot(Player player, ItemStack gun) {
        return findAmmo(player, gun, GunEnchantmentService.getCompressedAmmoUse(player, gun));
    }

    public static boolean hasEnoughAmmo(Player player, ItemStack gun, ItemStack selectedAmmo, int required) {
        if (selectedAmmo == null || selectedAmmo.isEmpty()) {
            return false;
        }
        if (GunFiringService.isInfinite(selectedAmmo) || selectedAmmo.getCount() >= required) {
            return true;
        }
        if (!(gun.getItem() instanceof BaseGun baseGun)) {
            return false;
        }

        List<ItemStack> copyList = new ArrayList<>(player.getInventory().items);
        GunEvent.InventoryExtraEvent inventoryExtraEvent = new GunEvent.InventoryExtraEvent(player, baseGun, copyList);
        NeoForge.EVENT_BUS.post(inventoryExtraEvent);
        return hasEnoughAmmo(inventoryExtraEvent.getAmmoList(), player, selectedAmmo, gun, required);
    }

    private static ItemStack findAmmo(Player player, ItemStack gun, int ammoUse) {
        if (!(gun.getItem() instanceof BaseGun baseGun)) {
            return ItemStack.EMPTY;
        }

        Inventory inventory = player.getInventory();
        ItemStack ammo = ItemStack.EMPTY;
        NonNullList<ItemStack> stackNonNullList = inventory.items;
        List<ItemStack> copyList = new ArrayList<>(stackNonNullList);

        GunEvent.InventoryExtraEvent inventoryExtraEvent = new GunEvent.InventoryExtraEvent(player, baseGun, copyList);
        NeoForge.EVENT_BUS.post(inventoryExtraEvent);

        for (ItemStack item : inventoryExtraEvent.getAmmoList()) {
            if (item == null || item.isEmpty() || item.is(Items.AIR)) continue;
            if (item.is(TGTags.AMMO) && isCompatible(player, item, gun)) {
                if (!GunFiringService.isInfinite(item)
                        && !hasEnoughAmmo(inventoryExtraEvent.getAmmoList(), player, item, gun, ammoUse)) {
                    continue;
                }
                ammo = item;
                break;
            }
        }
        return ammo;
    }

    private static boolean hasEnoughAmmo(List<ItemStack> ammoList, Player player, ItemStack selectedAmmo,
                                          ItemStack gun, int required) {
        int total = 0;
        for (ItemStack item : ammoList) {
            if (item == null || item.isEmpty() || item.is(Items.AIR)
                    || !ItemStack.isSameItemSameComponents(selectedAmmo, item)
                    || !item.is(TGTags.AMMO)
                    || !isCompatible(player, item, gun)) {
                continue;
            }
            total += item.getCount();
            if (total >= required) {
                return true;
            }
        }
        return false;
    }

    /**
     * Consumes one ammo type across inventory stacks, starting with the selected stack.
     */
    public static void consumeAmmo(Player player, ItemStack selectedAmmo, int amount) {
        if (selectedAmmo == null || selectedAmmo.isEmpty() || amount <= 0) {
            return;
        }

        NonNullList<ItemStack> inventoryItems = player.getInventory().items;
        boolean selectedInInventory = inventoryItems.stream().anyMatch(item -> item == selectedAmmo);
        if (!selectedInInventory) {
            selectedAmmo.shrink(amount);
            return;
        }

        int remaining = amount;
        for (ItemStack item : inventoryItems) {
            if (item.isEmpty() || !ItemStack.isSameItemSameComponents(selectedAmmo, item)) {
                continue;
            }
            int consumed = Math.min(remaining, item.getCount());
            item.shrink(consumed);
            remaining -= consumed;
            if (remaining <= 0) {
                return;
            }
        }
    }

    /**
     * 判断某个子弹是否与枪兼容
     */
    public static boolean isCompatible(Player player, ItemStack ammo, ItemStack gun) {
        if (ammo.isEmpty() || !(gun.getItem() instanceof BaseGun baseGun)) {
            return false;
        }

        boolean selected = ammo.getItem() instanceof BaseBullet;
        if (gun.is(TGItems.BLOWGUN)) selected = ammo.is(TGTags.SEED_AMMO);
        if (gun.is(TGItems.SNOWBALL_CANNON)) selected = ammo.is(TGTags.SNOW_AMMO);

        GunEvent.AmmoSelectionEvent ammoSelectionEvent = new GunEvent.AmmoSelectionEvent(player, baseGun, ammo, selected);
        NeoForge.EVENT_BUS.post(ammoSelectionEvent);
        return ammoSelectionEvent.isSelected();
    }

    /**
     * 是否可以开枪
     */
    public static boolean canShoot(Player player, ItemStack gun) {
        if (!(gun.getItem() instanceof BaseGun baseGun)) {
            return false;
        }

        ItemStack ammo = getAmmoForShot(player, gun);
        GunEvent.GunFireEvent gunFireEvent = new GunEvent.GunFireEvent(player, baseGun, ammo, !ammo.isEmpty());
        NeoForge.EVENT_BUS.post(gunFireEvent);

        return gunFireEvent.isFire();
    }
}
