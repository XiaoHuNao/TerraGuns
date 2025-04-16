package org.confluence.terra_guns.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.List;

public class GunEvent extends Event {
    private final Player player;
    private final BaseGun gun;

    public GunEvent(Player player, BaseGun gun) {
        this.player = player;
        this.gun = gun;
    }

    public BaseGun getGun() {
        return gun;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * 初始化开火事件
     */
    public static class UseGunEvent extends GunEvent implements ICancellableEvent {
        private int cooldowns;

        public UseGunEvent(Player player, BaseGun gun, int cooldowns) {
            super(player, gun);
            this.cooldowns = cooldowns;
        }

        public int getCooldowns() {
            return cooldowns;
        }

        public void setCooldowns(int cooldowns) {
            this.cooldowns = cooldowns;
        }
    }

    /**
     * 选择射击子弹
     */
    public static class AmmoSelectedEvent extends GunEvent {
        private ItemStack bullet;

        public AmmoSelectedEvent(Player player, BaseGun gun, ItemStack bullet) {
            super(player, gun);
            this.bullet = bullet;
        }

        public ItemStack getAmmo() {
            return bullet;
        }

        public void setAmmo(ItemStack bullet) {
            this.bullet = bullet;
        }
    }

    public static class AmmoDataEvent extends GunEvent {
        private float damage;
        private float knockback;
        private float velocity;
        private int penetrate;

        public AmmoDataEvent(Player player, BaseGun gun, float damage, float knockback, float velocity, int penetrate) {
            super(player, gun);
            this.damage = damage;
            this.knockback = knockback;
            this.velocity = velocity;
            this.penetrate = penetrate;
        }

        public float getDamage() {
            return damage;
        }

        public float getKnockback() {
            return knockback;
        }

        public float getVelocity() {
            return velocity;
        }

        public int getPenetrate() {
            return penetrate;
        }

        public void setDamage(float damage) {
            this.damage = damage;
        }

        public void setKnockback(float knockback) {
            this.knockback = knockback;
        }

        public void setVelocity(float velocity) {
            this.velocity = velocity;
        }

        public void setPenetrate(int penetrate) {
            this.penetrate = penetrate;
        }
    }

    /**
     * 子弹消耗
     */
    public static class ShrinkBulletEvent extends GunEvent implements ICancellableEvent {
        private int shrink = 1;
        private boolean infinity;
        private ItemStack bullet;
        private final ItemStack gun;

        public ShrinkBulletEvent(Player player, BaseGun baseGun, ItemStack gun, ItemStack bullet, boolean infinity) {
            super(player, baseGun);
            this.gun=gun;
            this.infinity = infinity;
            this.bullet = bullet;
        }

        public void setShrink(int shrink) {
            this.shrink = shrink;
        }

        public int getShrink() {
            return shrink;
        }

        public boolean isInfinity() {
            return infinity;
        }

        public void setInfinity(boolean infinity) {
            this.infinity = infinity;
        }

        public ItemStack getBulletStack() {
            return bullet;
        }

        public void setBulletStack(ItemStack bullet) {
            this.bullet = bullet;
        }

        public ItemStack getGunStack() {
            return gun;
        }
    }
}
