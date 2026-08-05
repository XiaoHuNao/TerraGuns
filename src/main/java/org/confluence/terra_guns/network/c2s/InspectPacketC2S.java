package org.confluence.terra_guns.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.gun.BaseGun;

public final class InspectPacketC2S implements CustomPacketPayload {
    private static final InspectPacketC2S INSTANCE = new InspectPacketC2S();
    public static final Type<InspectPacketC2S> TYPE = new Type<>(TerraGuns.asResource("inspect"));
    public static final StreamCodec<ByteBuf, InspectPacketC2S> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private InspectPacketC2S() {
    }

    @Override
    public Type<InspectPacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player
                    && !player.isSpectator()
                    && player.getMainHandItem().getItem() instanceof BaseGun gun) {
                gun.inspectAnimator(player.getMainHandItem(), player);
            }
        }).exceptionally(error -> {
            TerraGuns.LOGGER.error("Failed to process an inspect request", error);
            return null;
        });
    }

    public static void sendToServer() {
        PacketDistributor.sendToServer(INSTANCE);
    }
}
