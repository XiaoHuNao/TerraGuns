package org.confluence.terra_guns.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.combat.ShootingService;
import org.confluence.terra_guns.network.s2c.ShotFeedbackPacketS2C;


public final class ShootPacketC2S implements CustomPacketPayload {
    private static final ShootPacketC2S INSTANCE = new ShootPacketC2S();
    public static final Type<ShootPacketC2S> TYPE = new Type<>(TerraGuns.asResource("shoot"));
    public static final StreamCodec<ByteBuf, ShootPacketC2S> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ShootPacketC2S() {}

    @Override
    public Type<ShootPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (ShootingService.tryShoot(player)) {
                    ShotFeedbackPacketS2C.sendTo(player);
                }
            }
        }).exceptionally(e -> {
            TerraGuns.LOGGER.error("Failed to process a shooting request", e);
            return null;
        });
    }

    public static void sendToServer() {
        PacketDistributor.sendToServer(INSTANCE);
    }
}
