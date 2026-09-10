package org.confluence.terra_guns.client.renderer.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_guns.TerraGuns;

public final class SilverCrossEffect implements ActiveBulletVfx {
    private static final int LIFETIME = 5;
    private static final int COLOR = 0xFFFFFFFF;
    private static final float SIZE = 0.36F;
    private static final float GROWTH = 0.004F;
    private static final net.minecraft.resources.ResourceLocation TEXTURE =
            TerraGuns.asResource("textures/vfx/particles/star_06.png");
    private final Vec3 position;
    private int age;

    public SilverCrossEffect(Vec3 position) {
        this.position = position;
    }

    @Override
    public boolean tick(ClientLevel level) {
        age++;
        return age < LIFETIME;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPosition) {
        float fade = 1.0F - age / (float) LIFETIME;
        int color = BulletVfxRenderUtil.fadeColor(COLOR, fade);
        BulletVfxRenderUtil.sprite(poseStack, bufferSource, cameraPosition, position, TEXTURE,
                color, SIZE + age * GROWTH);
    }
}
