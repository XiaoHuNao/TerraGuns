package org.confluence.terra_guns.client.event;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.service.GeckoLibClient;
import software.bernie.geckolib.util.GeckoLibUtil;

@EventBusSubscriber(modid = TerraGuns.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameEvent {
    private static final Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void firstPersonHandRender(RenderHandEvent event) {
        minecraft.getModelManager();
        event.getEquipProgress();
    }

    @SubscribeEvent
    public static void firstPersonArmRender(RenderArmEvent event) {
        HumanoidArm arm = event.getArm();
    }

    @SubscribeEvent
    public static void cancelSwap(InputEvent.InteractionKeyMappingTriggered event) {
        if (minecraft.player.getItemInHand(event.getHand()).getItem() instanceof BaseGun) {
            event.setSwingHand(false);
//            event.setCanceled(true);
        }
    }
}
