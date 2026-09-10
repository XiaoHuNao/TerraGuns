package org.confluence.terra_guns.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.common.enchantment.GunEnchantmentService;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.network.s2c.BulletImpactPacketS2C;

@EventBusSubscriber(modid = TerraGuns.MODID)
public class TGGameEvents {
    @SubscribeEvent
    public static void bulletImpact(BulletEvent.HitEvent event) {
        if (!event.getBulletEntity().level().isClientSide) {
            BulletImpactPacketS2C.send(
                    event.getBulletEntity(),
                    event.getHitResult().getLocation()
            );
        }
    }

    @SubscribeEvent
    public static void swapGunAnimator(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (event.getSlot() == EquipmentSlot.MAINHAND && event.getFrom().getItem() instanceof BaseGun baseGun) {
                baseGun.putAwayAnimator(event.getFrom(), serverPlayer);
            }
            if (event.getSlot() == EquipmentSlot.MAINHAND && event.getTo().getItem() instanceof BaseGun baseGun) {
                // 切枪
                baseGun.pickAnimator(event.getTo(), serverPlayer);
//            TerraGuns.LOGGER.info(event.getTo().toString());
            }
            if (event.getSlot() == EquipmentSlot.MAINHAND && event.getFrom().getItem() instanceof BaseGun) {
                // 收枪
//            TerraGuns.LOGGER.info(event.getFrom().toString());
            }
        }
    }

    @SubscribeEvent
    public static void temporaryReserve(BulletEvent.DamageEntityEvent event) {
        if (!(event.getShooter() instanceof ServerPlayer player)
                || !(event.getTarget() instanceof LivingEntity target)) {
            return;
        }
        if (event.getBulletEntity().getBulletStack().is(TGItems.ENDLESS_MUSKET_POUCH)) {
            return;
        }

        int level = event.getBulletEntity().getTemporaryReserveLevel();
        float chance = GunEnchantmentService.getTemporaryReserveChance(level);
        if (chance <= 0.0F || player.getRandom().nextFloat() >= chance) {
            return;
        }

        int refund = Mth.floor(target.getArmorValue()
                + target.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        if (refund <= 0) {
            return;
        }

        ItemStack ammo = event.getBulletEntity().getBulletStack().copyWithCount(refund);
        if (ammo.isEmpty()) {
            return;
        }
        if (!player.addItem(ammo) && !ammo.isEmpty()) {
            player.drop(ammo, false);
        }
    }

    @SubscribeEvent
    public static void protectBulletOwnerFromExplosion(ExplosionEvent.Detonate event) {
        if (!(event.getExplosion().getDirectSourceEntity() instanceof BaseBulletEntity bullet)) {
            return;
        }

        net.minecraft.world.entity.Entity owner = bullet.getOwner();
        if (owner != null) {
            event.getAffectedEntities().removeIf(entity -> entity == owner || entity.isPassengerOfSameVehicle(owner));
        }
    }
}
