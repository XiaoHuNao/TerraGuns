package org.confluence.terra_guns.common.item.gun;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.api.IBullet;
import org.confluence.terra_guns.api.IGun;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

// 散弹枪
public class ShotgunItem extends GunItem implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
     private int bulletCount;

    public ShotgunItem(float projectileSpeed, int bulletCount, float inaccuracy) {
        super(projectileSpeed, inaccuracy);
        this.bulletCount = bulletCount;
    }

    public ShotgunItem(int bulletCount,float inaccuracy) {
        this.bulletCount = bulletCount;
        setInaccuracy(inaccuracy);
    }

    @Override
    public void serverShoot(ServerLevel level, Player player, ItemStack gunStack, ItemStack ammoStack, IBullet bullet, IGun gun, boolean bulletFree) {
        for (int i = 0; i < bulletCount; i++) {
            super.serverShoot(level, player, gunStack, ammoStack, bullet, gun, bulletFree || i > 0);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }
}
