package org.confluence.terra_guns.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.GunEvent;

/** Server acknowledgement used to drive client-only fire feedback. */
public final class ShotFeedbackPacketS2C implements CustomPacketPayload {
    private static final ShotFeedbackPacketS2C INSTANCE = new ShotFeedbackPacketS2C();
    public static final Type<ShotFeedbackPacketS2C> TYPE = new Type<>(TerraGuns.asResource("shot_feedback"));
    public static final StreamCodec<ByteBuf, ShotFeedbackPacketS2C> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ShotFeedbackPacketS2C() {
    }

    @Override
    public Type<ShotFeedbackPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> NeoForge.EVENT_BUS.post(new GunEvent.ShotConfirmedEvent(context.player())));
    }

    public static void sendTo(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, INSTANCE);
    }
}
