package org.confluence.terra_guns.network.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.common.definition.BulletImpactEffect;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public record BulletImpactPacketS2C(double x, double y, double z, int effect) implements CustomPacketPayload {
    public static final Type<BulletImpactPacketS2C> TYPE = new Type<>(TerraGuns.asResource("bullet_impact"));
    public static final StreamCodec<ByteBuf, BulletImpactPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, BulletImpactPacketS2C::x,
            ByteBufCodecs.DOUBLE, BulletImpactPacketS2C::y,
            ByteBufCodecs.DOUBLE, BulletImpactPacketS2C::z,
            ByteBufCodecs.VAR_INT, BulletImpactPacketS2C::effect,
            BulletImpactPacketS2C::new
    );

    @Override
    public Type<BulletImpactPacketS2C> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> NeoForge.EVENT_BUS.post(
                new BulletEvent.ImpactEffectEvent(new Vec3(x, y, z), BulletImpactEffect.byId(effect))
        ));
    }

    public static void send(BaseBulletEntity entity, Vec3 position) {
        if (!(entity.level() instanceof ServerLevel level)) return;
        int effect = entity.getBullet().getImpactEffect().id();
        if (effect == BulletImpactEffect.NONE.id()) return;

        PacketDistributor.sendToPlayersNear(
                level,
                null,
                position.x,
                position.y,
                position.z,
                64.0D,
                new BulletImpactPacketS2C(position.x, position.y, position.z, effect)
        );
    }
}
