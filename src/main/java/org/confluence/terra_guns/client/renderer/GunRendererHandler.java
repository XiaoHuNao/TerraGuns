package org.confluence.terra_guns.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import java.util.NoSuchElementException;
import java.util.Objects;

public class GunRendererHandler<T extends BaseGun> extends GeoItemRenderer<T> implements IClientItemExtensions {
    public static final GeoBone EMPTY = new GeoBone(null, "empty", false, 0d, true, false);
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
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        var handRenderer = minecraft.gameRenderer.itemInHandRenderer;
        var entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        var playerrenderer = (PlayerRenderer) entityRenderDispatcher.getRenderer(player);

        if (Objects.equals(bone.getName(), "right_hand")) {
            poseStack.pushPose();
            RenderUtil.translateToPivotPoint(poseStack, bone);
            RenderUtil.rotateMatrixAroundBone(poseStack, bone);
            RenderUtil.translateAwayFromPivotPoint(poseStack, bone);

            Matrix3f normalisedPoseState = poseStack.last().normal();
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());
            poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            poseStack.mulPose(Axis.XP.rotationDegrees(90f));

            PoseStack poseStack1 = new PoseStack();
            poseStack1.last().pose().mul(poseState);
            poseStack1.last().pose().normal(normalisedPoseState);
            playerrenderer.renderRightHand(poseStack, bufferSource, packedLight, player);
            poseStack.popPose();
        } else if (Objects.equals(bone.getName(), "left_hand")) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            poseStack.mulPose(Axis.XP.rotationDegrees(90f));

            playerrenderer.renderLeftHand(poseStack, bufferSource, packedLight, player);
            poseStack.popPose();
        } else {
//            super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
        }
    }

    @Override
    public boolean applyForgeHandTransform(@NotNull PoseStack poseStack, @NotNull LocalPlayer player, @NotNull HumanoidArm arm, @NotNull ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        GeoBone camera = this.model.getBone("camera").orElse(EMPTY);
        RenderUtil.translateAwayFromPivotPoint(poseStack, camera);
//        testQuad(bufferSource, poseStack);
        return true;
    }

    private void testQuad(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.DEBUG_QUADS);
        PoseStack.Pose last = poseStack.last();
        //x 为深轴
        consumer.addVertex(last, 0.0f, 0.0f, 0.3f).setColor(255, 255, 255, 255);
        consumer.addVertex(last, 0.0f, 0.5f, 0.3f).setColor(255, 255, 255, 255);
        consumer.addVertex(last, 0.0f, 0.8f, 0.0f).setColor(255, 0, 255, 255);
        consumer.addVertex(last, 0.0f, 0.7f, 0.0f).setColor(255, 255, 255, 255);
        bufferSource.endBatch();
    }
}