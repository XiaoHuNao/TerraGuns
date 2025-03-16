package org.confluence.terra_guns.common.item.gun;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.api.IAmmo;
import org.confluence.terra_guns.api.IGun;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;

// 散弹枪
public class ShotgunItem extends GeoGunItem<BaseAmmoEntity> {
    private final int bulletCount;

    public ShotgunItem(int bulletCount, float inaccuracy) {
        this.bulletCount = bulletCount;
        this.inaccuracy = inaccuracy;
    }
    public ShotgunItem(Properties properties, int bulletCount, float inaccuracy) {
        super(properties);
        this.bulletCount = bulletCount;
        this.inaccuracy = inaccuracy;
    }

    @Override
    public void serverShoot(ServerLevel level, Player player, ItemStack gunStack, ItemStack ammoStack, IAmmo<BaseAmmoEntity> ammo, IGun<BaseAmmoEntity> gun, boolean infiniteAmmo) {
        for (int i = 0; i < bulletCount && !ammoStack.isEmpty(); i++) {
            super.serverShoot(level, player, gunStack, ammoStack, ammo, gun, infiniteAmmo || i > 0);
        }
    }
}
