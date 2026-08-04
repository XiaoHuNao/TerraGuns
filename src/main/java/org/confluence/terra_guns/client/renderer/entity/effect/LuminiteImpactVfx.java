package org.confluence.terra_guns.client.renderer.entity.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public final class LuminiteImpactVfx implements BulletImpactVfx {
    public static final LuminiteImpactVfx INSTANCE = new LuminiteImpactVfx();
    private static final int COUNT = 16;

    private LuminiteImpactVfx() {
    }

    @Override
    public void play(Vec3 position) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        for (int index = 0; index < COUNT; index++) {
            double angle = Math.PI * 2.0D * index / COUNT;
            double speed = 0.025D + level.random.nextDouble() * 0.05D;
            level.addParticle(
                    ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                    position.x + (level.random.nextDouble() - 0.5D) * 0.20D,
                    position.y + (level.random.nextDouble() - 0.5D) * 0.20D,
                    position.z + (level.random.nextDouble() - 0.5D) * 0.20D,
                    Math.cos(angle) * speed,
                    0.025D + level.random.nextDouble() * 0.045D,
                    Math.sin(angle) * speed
            );
        }
    }
}
