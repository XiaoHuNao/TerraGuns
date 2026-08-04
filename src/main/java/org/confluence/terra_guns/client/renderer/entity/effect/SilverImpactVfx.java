package org.confluence.terra_guns.client.renderer.entity.effect;

import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.client.renderer.entity.BulletVfxManager;

public final class SilverImpactVfx implements BulletImpactVfx {
    public static final SilverImpactVfx INSTANCE = new SilverImpactVfx();

    private SilverImpactVfx() {
    }

    @Override
    public void play(Vec3 position) {
        BulletVfxManager.add(new SilverCrossEffect(position));
    }
}
