package org.confluence.terra_guns.network.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.enchantment.GunEnchantmentService;
import org.confluence.terra_guns.common.item.gun.BaseGun;

public record EmergencyMeleePacketC2S(int targetId) implements CustomPacketPayload {
    public static final Type<EmergencyMeleePacketC2S> TYPE = new Type<>(TerraGuns.asResource("emergency_melee"));
    public static final StreamCodec<ByteBuf, EmergencyMeleePacketC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, EmergencyMeleePacketC2S::targetId,
            EmergencyMeleePacketC2S::new
    );

    @Override
    public Type<EmergencyMeleePacketC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player) || player.isSpectator()) {
                return;
            }

            ItemStack gunStack = player.getMainHandItem();
            if (!(gunStack.getItem() instanceof BaseGun gun)) {
                return;
            }

            Entity target = player.level().getEntity(targetId);
            if (target != null) {
                GunEnchantmentService.tryEmergencyMelee(gun, gunStack, player, target);
            }
        }).exceptionally(error -> {
            TerraGuns.LOGGER.error("Failed to process an emergency melee request", error);
            return null;
        });
    }

    public static void sendToServer(int targetId) {
        PacketDistributor.sendToServer(new EmergencyMeleePacketC2S(targetId));
    }
}
