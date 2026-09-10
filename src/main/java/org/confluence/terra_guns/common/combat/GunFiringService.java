package org.confluence.terra_guns.common.combat;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;
import org.confluence.terra_guns.common.enchantment.GunEnchantmentService;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.util.TGUtil;

import java.util.function.Supplier;

/**
 * Resolves one gun/ammunition pair and sends it to the projectile factory.
 * Inventory, cooldown and input validation remain in {@link ShootingService}.
 */
public final class GunFiringService {
    private GunFiringService() {
    }

    public static int fire(ServerPlayer player, BaseGun gun, ItemStack gunStack, ItemStack ammo) {
        if (ammo == null) {
            return 0;
        }

        ShotContext context = resolve(player, gun, gunStack, ammo);
        return GunProjectileFactory.spawn(context, gun.getDefinition().projectilePattern());
    }

    /** Fires a fixed radial burst without consuming the selected ammunition. */
    public static int fireRadial(ServerPlayer player, BaseGun gun, ItemStack gunStack, ItemStack ammo, int directions) {
        if (ammo == null || ammo.isEmpty()) {
            return 0;
        }

        ShotContext context = resolve(player, gun, gunStack, ammo);
        return GunProjectileFactory.spawnRadial(context, gun.getDefinition().projectilePattern(), directions);
    }

    private static ShotContext resolve(ServerPlayer player, BaseGun gun, ItemStack gunStack, ItemStack ammo) {

        GunPropertyComponent gunProperties = gunStack.get(TGDataComponents.GUN_PROPERTY_COMPONENT);
        if (gunProperties == null) {
            gunProperties = gun.getDefinition().component();
        }

        BulletPropertyComponent ammoProperties = ammo.get(TGDataComponents.BULLET_PROPERTY_COMPONENT);
        if (ammoProperties == null && ammo.getItem() instanceof BaseBullet bullet) {
            // Item stacks saved before the data component was introduced may
            // not carry the component. Use the immutable item definition so
            // their damage, velocity and penetration are still respected.
            ammoProperties = bullet.getDefinition().component();
        }
        if (ammoProperties == null) {
            ammoProperties = BulletPropertyComponent.EMPTY;
        }

        Ballistics ballistics = BallisticsResolver.resolve(
                new GunStats(
                        gunProperties.damage(),
                        gunProperties.velocity(),
                        gunProperties.knockback(),
                        gunProperties.critical(),
                        gunProperties.penetrate(),
                        gun.getDefinition().inaccuracy()
                ),
                new AmmoStats(
                        ammoProperties.damage(),
                        ammoProperties.velocity(),
                        ammoProperties.velocityMultiplier(),
                        ammoProperties.knockback(),
                        ammoProperties.penetrate()
                )
        );

        GunEvent.AmmoDataEvent ammoDataEvent = new GunEvent.AmmoDataEvent(
                player,
                gun,
                gunStack,
                ballistics.damage(),
                ballistics.critical(),
                ballistics.knockback(),
                ballistics.velocity(),
                ballistics.penetrate(),
                ballistics.inaccuracy()
        );
        NeoForge.EVENT_BUS.post(ammoDataEvent);

        float finalDamage = TGUtil.criticalDamageTotal(ammoDataEvent.getCritical(), ammoDataEvent.getDamage(), player.getRandom());
        finalDamage *= GunEnchantmentService.getCompressedDamageMultiplier(player, gunStack);
        return new ShotContext(
                player,
                gunStack,
                ammo,
                finalDamage,
                ammoDataEvent.getKnockback(),
                ammoDataEvent.getVelocity(),
                ammoDataEvent.getPenetrate(),
                ammoDataEvent.getInaccuracy()
        );
    }

    public static boolean isInfinite(ItemStack ammo) {
        Supplier<DataComponentType<BulletPropertyComponent>> componentType = TGDataComponents.BULLET_PROPERTY_COMPONENT;
        BulletPropertyComponent component = ammo.get(componentType);
        return component != null && component.infinity();
    }
}
