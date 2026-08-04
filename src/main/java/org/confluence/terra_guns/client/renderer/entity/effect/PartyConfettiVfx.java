package org.confluence.terra_guns.client.renderer.entity.effect;

import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.client.renderer.entity.BulletVfxManager;

public final class PartyConfettiVfx implements BulletImpactVfx {
    public static final PartyConfettiVfx INSTANCE = new PartyConfettiVfx();

    private PartyConfettiVfx() {
    }

    @Override
    public void play(Vec3 position) {
        BulletVfxManager.add(new PartyConfettiEffect(position));
    }
}
