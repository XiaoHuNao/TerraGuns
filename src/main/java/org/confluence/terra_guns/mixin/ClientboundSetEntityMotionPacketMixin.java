package org.confluence.terra_guns.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSetEntityMotionPacket.class)
public abstract class ClientboundSetEntityMotionPacketMixin {
    @Mutable
    @Shadow
    @Final
    private int xa;

    @Mutable
    @Shadow
    @Final
    private int ya;

    @Mutable
    @Shadow
    @Final
    private int za;

    @WrapOperation(method = "<init>(ILnet/minecraft/world/phys/Vec3;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(DDD)D"))
    private double unlimited(double value, double min, double max, Operation<Double> original) {
        return original.call(value, -0x3f3f3f3f / 8000.0, 0x3f3f3f3f / 8000.0);
    }

    @WrapOperation(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readShort()S"))
    private short cancel(FriendlyByteBuf instance, Operation<Short> original) {
        return 0;
    }

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At("TAIL"))
    private void readVarInt(FriendlyByteBuf buffer, CallbackInfo ci) {
        this.xa = buffer.readVarInt();
        this.ya = buffer.readVarInt();
        this.za = buffer.readVarInt();
    }

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeShort(I)Lnet/minecraft/network/FriendlyByteBuf;", ordinal = 0), cancellable = true)
    private void writeVarInt(FriendlyByteBuf buffer, CallbackInfo ci) {
        buffer.writeVarInt(xa);
        buffer.writeVarInt(ya);
        buffer.writeVarInt(za);
        ci.cancel();
    }
}
