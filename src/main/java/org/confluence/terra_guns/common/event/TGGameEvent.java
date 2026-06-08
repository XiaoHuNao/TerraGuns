package org.confluence.terra_guns.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.terra_guns.common.init.TGDamageTypes;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.living.PortLivingEquipmentChangeEvent;
import org.mesdag.portlib.event.entity.living.PortLivingIncomingDamageEvent;

public class TGGameEvent {
    public static void init() {
        PortEventHandler.addListener(TGGameEvent::livingEquipmentChange);
        PortEventHandler.addListener(TGGameEvent::livingIncomingDamage);
    }

    private static void livingEquipmentChange(PortLivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getSlot() == EquipmentSlot.MAINHAND && event.getTo().getItem() instanceof BaseGun baseGun) {
                // 切枪
                baseGun.pickAnimator(event.getTo(), player);
//            TerraGuns.LOGGER.info(event.getTo().toString());
            }
            if (event.getSlot() == EquipmentSlot.MAINHAND && event.getFrom().getItem() instanceof BaseGun) {
                // 收枪
//            TerraGuns.LOGGER.info(event.getFrom().toString());
            }
        }
    }

    private static void livingIncomingDamage(PortLivingIncomingDamageEvent event) {
        if (event.getSource().is(TGDamageTypes.BULLET_DAMAGE)) {
            if (ConfluenceMagicLib.IS_CONFLUENCE_LOAD) return;
            event.setInvulnerabilityTicks(0);
        }
    }
}
