package org.confluence.terra_guns.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletManager;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.List;

public record ShootPacketC2S() implements CustomPacketPayload {
    public static final Type<ShootPacketC2S> TYPE = new Type<>(TerraGuns.asResource("shoot"));
    public static final StreamCodec<ByteBuf, ShootPacketC2S> STREAM_CODEC = StreamCodec.unit(new ShootPacketC2S());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                ItemStack gunStack = serverPlayer.getMainHandItem();
                if (gunStack.getItem() instanceof BaseGun baseGun) {
                    BulletManager bulletManager = new BulletManager(serverPlayer);
                    ItemStack ammo = bulletManager.getAmmo();

                    baseGun.shoot(serverPlayer, ammo);
                    baseGun.triggerAnim(serverPlayer, GeoItem.getOrAssignId(gunStack, serverPlayer.serverLevel()), "controller", "fire");

                    boolean infinity = ammo.get(TGDataComponents.BULLET_PROPERTY_COMPONENT).infinity();
                    GunEvent.ShrinkBulletEvent shrinkBulletEvent = new GunEvent.ShrinkBulletEvent(serverPlayer, baseGun, ammo, gunStack, infinity);
                    NeoForge.EVENT_BUS.post(shrinkBulletEvent);

                    if (!shrinkBulletEvent.isInfinity() && !shrinkBulletEvent.isCanceled()) {
                        shrinkBulletEvent.getBulletStack().shrink(shrinkBulletEvent.getShrink());
                    }
                }
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToServer() {
        PacketDistributor.sendToServer(new ShootPacketC2S());
    }
}
