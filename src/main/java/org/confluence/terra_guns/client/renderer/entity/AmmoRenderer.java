package org.confluence.terra_guns.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.client.model.entity.AmmoModel;
import org.confluence.terra_guns.common.entity.AmmoEntity;

public class AmmoRenderer extends EntityRenderer<AmmoEntity> {
    private static final ResourceLocation TEXTURES = TerraGuns.asResource("textures/entity/ammo/ammo.png");

    private final AmmoModel model;

    public AmmoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new AmmoModel(pContext.bakeLayer(AmmoModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(AmmoEntity pEntity) {
        return TEXTURES;
    }

    @Override
    public void render(AmmoEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight) {
        poseStack.pushPose();
        model.renderToBuffer(poseStack, pBuffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(pEntity))), 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}