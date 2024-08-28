package org.confluence.terra_guns.common.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.IGun;
import org.confluence.terra_guns.common.network.NetworkHandler;
import org.confluence.terra_guns.common.network.c2s.GunShootingPacketC2S;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TerraGuns.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {
//    @SubscribeEvent
//    public static void leftClick(InputEvent.InteractionKeyMappingTriggered event) {
//        LocalPlayer localPlayer = Minecraft.getInstance().player;
//        if (localPlayer == null) return;
//        InteractionHand hand = event.getHand();
//        ItemStack itemInHand = localPlayer.getItemInHand(hand);
//        if (itemInHand.getItem() instanceof IGun gunItem) {
//            NetworkHandler.NETWORK.sendToServer(new GunShootingPacketC2S(itemInHand,hand));
//            event.setSwingHand(false);
//        }
//    }
//
//
//
//    private static final int MAX_CHARGE_TIME = 1000; // 最大蓄力时间（毫秒）
//    private static long chargeStartTime = 0; // 蓄力开始时间
//
//    @SubscribeEvent
//    public static void onMouseEvent(InputEvent.MouseButton.Pre event) {
//        if (event.getAction() == InputConstants.PRESS) {
//            // 鼠标按钮被按下
//            if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
//                // 左键被按下，开始蓄力
//                chargeStartTime = System.currentTimeMillis();
//            }
//        } else if (event.getAction() == InputConstants.RELEASE) {
//            // 鼠标按钮被释放
//            if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
//                // 左键被释放，计算蓄力时间
//                long chargeTime = System.currentTimeMillis() - chargeStartTime;
//                // 根据蓄力时间执行相应的操作
//                if (chargeTime >= MAX_CHARGE_TIME) {
//                    // 蓄力时间达到最大值，执行最大蓄力操作
//                    System.out.println("最大蓄力操作");
//                } else {
//                    // 蓄力时间未达到最大值，执行普通操作
//                    System.out.println("普通操作");
//                }
//                // 重置蓄力开始时间
//                chargeStartTime = 0;
//            }
//        }
//    }
}
