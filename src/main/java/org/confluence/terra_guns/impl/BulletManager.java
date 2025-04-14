package org.confluence.terra_guns.impl;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.common.init.TGTags;

public class BulletManager {
    private final Inventory inventory;

    public BulletManager(Player player) {
        this(player.getInventory());
    }

    public BulletManager(Inventory inventory) {
        this.inventory = inventory;
    }

    public ItemStack getAmmo(){
        for (ItemStack item : inventory.items) {
            if (item.is(TGTags.AMMO)){
                return item;
            }
        }
        return ItemStack.EMPTY;
    }
}
