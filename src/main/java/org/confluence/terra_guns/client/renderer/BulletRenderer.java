package org.confluence.terra_guns.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public class BulletRenderer extends EntityRenderer<BaseBulletEntity> {

    protected BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseBulletEntity baseBulletEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(BaseBulletEntity baseBulletEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
    }
}
