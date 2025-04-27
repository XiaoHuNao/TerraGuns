package org.confluence.terra_guns.impl;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.init.TGTags;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.common.item.gun.BaseGun;

public class BulletHandler {
    /**
     * 获取玩家背包中第一个兼容该枪的子弹
     */
    public static ItemStack getAmmo(Player player, ItemStack gun) {
        Inventory inventory = player.getInventory();
        ItemStack ammo = ItemStack.EMPTY;
        for (ItemStack item : inventory.items) {
            if (item.is(TGTags.AMMO) && isCompatible(item, gun)) {
                ammo = item;
                break;
            }
        }
        return ammo;
    }

    /**
     * 判断某个子弹是否与枪兼容
     */
    public static boolean isCompatible(ItemStack ammo, ItemStack gun) {
        return ammo.getItem() instanceof BaseBullet;
    }

    /**
     * 是否可以开枪
     */
    public static boolean canShoot(Player player, ItemStack gun) {
        ItemStack ammo = getAmmo(player, gun);
        GunEvent.GunFireEvent gunFireEvent = new GunEvent.GunFireEvent(player, (BaseGun) gun.getItem(), ammo, false);
        NeoForge.EVENT_BUS.post(gunFireEvent);
        return !(ammo.isEmpty() && !gunFireEvent.isCanceled()) || gunFireEvent.isAlwaysFire();
    }
}
