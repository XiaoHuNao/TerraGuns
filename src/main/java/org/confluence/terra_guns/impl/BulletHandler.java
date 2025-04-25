package org.confluence.terra_guns.impl;

import com.mojang.datafixers.util.Pair;
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
    public static ItemStack getAmmo(Player player, BaseGun gun) {
        Inventory inventory = player.getInventory();
        for (ItemStack item : inventory.items) {
            if (item.is(TGTags.AMMO) && isCompatible(item)) {
                GunEvent.AmmoSelectedEvent ammoSelectedEvent = new GunEvent.AmmoSelectedEvent(player, gun, item);
                NeoForge.EVENT_BUS.post(ammoSelectedEvent);
                return ammoSelectedEvent.getAmmo();
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * 判断某个子弹是否与枪兼容（目前只判断是不是 BaseBullet 实例）
     */
    public static boolean isCompatible(ItemStack ammo) {
        return ammo.getItem() instanceof BaseBullet;
    }

    /**
     * 是否可以开枪（是否找到有效弹药）
     */
    public static boolean canShoot(Player player, BaseGun gun) {
        return !getAmmo(player, gun).isEmpty();
    }
}
