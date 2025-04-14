package org.confluence.terra_guns.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemCooldowns;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.init.TGKeys;
import org.confluence.terra_guns.client.sounds.SoundsManager;
import org.confluence.terra_guns.common.item.gun.BaseGun;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameEvent {
    private static final Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void gunShot(ClientTickEvent.Post event) {
        if (TGKeys.SHOOT.get().consumeClick()) {
            LocalPlayer player = minecraft.player;
            ClientLevel level = minecraft.level;
            ItemCooldowns cooldowns = player.getCooldowns();
            if (player.getMainHandItem().getItem() instanceof BaseGun baseGun && !cooldowns.isOnCooldown(baseGun)) {
                player.playSound(SoundsManager.getSound(player.getMainHandItem()), 1f, 1f);

                cooldowns.addCooldown(baseGun, baseGun.getCooldown());
            }
        }
    }

    @SubscribeEvent
    public static void cancelSwap(InputEvent.InteractionKeyMappingTriggered event) {
        LocalPlayer player = minecraft.player;
        if (player.getItemInHand(event.getHand()).getItem() instanceof BaseGun) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }
}
