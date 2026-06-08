package org.confluence.terra_guns.network.c2s;

import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletHandler;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public enum ShootPacketC2S implements IPortPacket.C2S {
    INSTANCE;

    public static final ResourceLocation ID = TerraGuns.asResource("shoot");
    public static final PortStreamCodec<ByteBuf, ShootPacketC2S> STREAM_CODEC = PortStreamCodec.unit(INSTANCE);

    @Override
    public void work(ServerPlayer player) {
        ItemStack gunStack = player.getMainHandItem();
        if (gunStack.getItem() instanceof BaseGun baseGun) {
            ItemStack ammo = BulletHandler.getAmmo(player, gunStack);
            ammo = ammo.equals(ItemStack.EMPTY) ? TGItems.EMPTY_BULLET.get().getDefaultInstance() : ammo;

            baseGun.shoot(player, ammo, gunStack);
            baseGun.fireAnimator(gunStack, player);

            BulletPropertyComponent component = PortItemStackExtension.getData(ammo, TGDataComponents.BULLET_PROPERTY_COMPONENT);
            boolean infinity = component != null && component.infinity();
            GunEvent.ShrinkBulletEvent event = new GunEvent.ShrinkBulletEvent(player, baseGun, gunStack, ammo, infinity);
            PortEventHandler.postEvent(event);

            if (!event.isInfinity() && !event.isCanceled()) {
                event.getBulletStack().shrink(event.getShrink());
            }
        }
    }

    @Override
    public ResourceLocation identifier() {
        return null;
    }

    public static void sendToServer() {
        TerraGuns.NETWORK_HANDLER.sendToServer(INSTANCE);
    }
}
