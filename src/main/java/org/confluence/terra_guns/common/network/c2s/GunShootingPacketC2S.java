package org.confluence.terra_guns.common.network.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.confluence.terra_guns.api.IGun;

import java.util.function.Supplier;

public record GunShootingPacketC2S(ItemStack gunStack, InteractionHand hand) {
    public static Codec<GunShootingPacketC2S> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("gunStack").forGetter(GunShootingPacketC2S::gunStack),
            Codec.STRING.fieldOf("hand").xmap(InteractionHand::valueOf, InteractionHand::name).forGetter(GunShootingPacketC2S::hand)
    ).apply(instance, GunShootingPacketC2S::new));


    public static void encode(GunShootingPacketC2S packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeJsonWithCodec(CODEC, packet);
    }

    public static GunShootingPacketC2S decode(FriendlyByteBuf friendlyByteBuf) {
        return friendlyByteBuf.readJsonWithCodec(CODEC);
    }

    public static void serverHandle(GunShootingPacketC2S packet, Supplier<NetworkEvent.Context> ctx) {
//        NetworkEvent.Context context = ctx.get();
//        context.enqueueWork(() -> {
//            ServerPlayer player = context.getSender();
//            Level level = player.level();
//            if (player == null) return;
//            ItemStack gunStack = packet.gunStack;
//            ItemStack ammoStack = player.getProjectile(gunStack);
//            boolean flag = !ammoStack.isEmpty();
//
//            if (gunStack.getItem() instanceof IGun gunItem){
//                if (!player.getAbilities().instabuild && !flag) {
//                } else {
//                    player.startUsingItem(packet.hand);
//                }
//            }
//        });
//        context.setPacketHandled(true);
    }
}
