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

import java.util.ArrayList;
import java.util.List;

public class BulletManager {
    private final Player player;
    private final Inventory inventory;
    private final BaseGun gun;

    public BulletManager(Player player) {
        this(player, player.getInventory(), (BaseGun) player.getMainHandItem().getItem());
    }

    public BulletManager(Player player, BaseGun gun) {
        this(player, player.getInventory(), gun);
    }

    public BulletManager(Player player, Inventory inventory, BaseGun gun) {
        this.player = player;
        this.inventory = inventory;
        this.gun = gun;
    }

    public List<ItemStack> getAmmo() {
        List<ItemStack> ammo = new ArrayList<>();
        for (ItemStack item : inventory.items) {
            if (item.is(TGTags.AMMO) && isCompatible(item)) {
                ammo.add(item);
            }
        }
        GunEvent.AmmoSelectedEvent event = new GunEvent.AmmoSelectedEvent(player, gun, ammo);
        NeoForge.EVENT_BUS.post(event);
        return ammo;
    }

    public boolean canShoot() {
        return !getAmmo().isEmpty();
    }

    /**
     * 判断某种子弹是否与该枪兼容
     */
    public boolean isCompatible(ItemStack ammo) {
        return ammo.getItem() instanceof BaseBullet;
    }
}
