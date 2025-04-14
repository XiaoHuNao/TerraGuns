package org.confluence.terra_guns.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.impl.BulletManager;

public record ShootPacketC2S(int penetrate) implements CustomPacketPayload {
    public static final Type<ShootPacketC2S> TYPE = new Type<>(TerraGuns.asResource("shoot"));
    public static final StreamCodec<ByteBuf, ShootPacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.penetrate,
            ShootPacketC2S::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer){
                ItemStack ammo = new BulletManager(serverPlayer).getAmmo();

                if (serverPlayer.getMainHandItem().getItem() instanceof BaseGun baseGun){
//                    baseGun.shoot(serverPlayer.serverLevel(), serverPlayer, );
                }
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }

    public static void sendToServer(int index) {
        PacketDistributor.sendToServer(new ShootPacketC2S(index));
    }
}
