package org.confluence.terra_guns.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.init.TGDamageTypes;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.network.s2c.BulletImpactPacketS2C;

@EventBusSubscriber(modid = TerraGuns.MODID)
public class GameEvent {
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
    public static void swapGunAnimator(LivingEquipmentChangeEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
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
    public static void hurtEvent(LivingIncomingDamageEvent event){
        if (event.getSource().is(TGDamageTypes.BULLET_DAMAGE)) {
            if (ConfluenceMagicLib.IS_CONFLUENCE_LOAD) return;
            event.setInvulnerabilityTicks(0);
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
