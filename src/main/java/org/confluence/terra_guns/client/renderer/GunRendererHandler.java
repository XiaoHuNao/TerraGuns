package org.confluence.terra_guns.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.confluence.terra_guns.client.model.item.GunModel;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GunRendererHandler<T extends BaseGun> extends GeoItemRenderer<T> implements IClientItemExtensions {
    private GunRendererHandler<T> renderer;

    public GunRendererHandler(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        super(new GunModel<>(model, texture, animation));
    }

    public GunRendererHandler(ResourceLocation gun) {
        super(new GunModel<>(gun));
    }

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        if (renderer == null) {
            this.renderer = this;
        }
        return renderer;
    }


    @Override
    public boolean applyForgeHandTransform(@NotNull PoseStack poseStack, @NotNull LocalPlayer player, @NotNull HumanoidArm arm, @NotNull ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        Minecraft minecraft = Minecraft.getInstance();
        var handRenderer = minecraft.gameRenderer.itemInHandRenderer;
        var bufferSource = minecraft.renderBuffers().bufferSource();
        var entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        int packedLight = entityRenderDispatcher.getPackedLightCoords(minecraft.player, partialTick);

        PlayerRenderer playerrenderer = (PlayerRenderer) entityRenderDispatcher.getRenderer(player);


        poseStack.pushPose();
        poseStack.translate(-0.025F, -0.725F, -0.2F);
        poseStack.mulPose(Axis.XN.rotationDegrees(60.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.translate(-0.1F, 0.0F, 0.0F);

//        poseStack.mulPose(Axis.XP.rotation(-6.283F));
        poseStack.translate(-0.3F, 0.0F, 0.0F);
//        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0.3F, 0.0F, 0.0F);

        playerrenderer.renderRightHand(poseStack, bufferSource, packedLight, player);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(-0.025F, -0.725F, -0.2F);
        poseStack.mulPose(Axis.XN.rotationDegrees(60.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.translate(0.1F, 0.0F, 0.0F);
        playerrenderer.renderLeftHand(poseStack, bufferSource, packedLight, player);
        poseStack.popPose();


        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(i * 0.56F, -0.52F, -0.72F);
        return true;
    }
}