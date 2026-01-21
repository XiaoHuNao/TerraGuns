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
import net.minecraft.world.entity.player.Player;
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
    public void renderRecursively(
            PoseStack poseStack,
            T animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        poseStack.pushPose();

        // GeckoLib 应用当前骨骼矩阵
        RenderUtil.prepMatrixForBone(poseStack, bone);

        String name = bone.getName();

        // ================
        //   手部替换方案
        // ================
        if (name.equals("right_hand") || name.equals("left_hand")) {

            // 不执行默认 cubes 渲染（跳过）
            // 但需要渲染手部模型
            poseStack.pushPose();

            Minecraft mc = Minecraft.getInstance();
            var player = mc.player;
            PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);

            // 可在此加入额外变换让手模型对齐你的骨骼
            // poseStack.mulPose(...)

            if (name.equals("right_hand")) {
                playerRenderer.renderRightHand(poseStack, bufferSource, packedLight, player);
            } else {
                playerRenderer.renderLeftHand(poseStack, bufferSource, packedLight, player);
            }

            poseStack.popPose();

            // ✔ 不渲染 cubes
            // ✔ 但继续渲染子骨骼（可以有手指等）
            for (GeoBone child : bone.getChildBones()) {
                renderRecursively(poseStack, animatable, child,
                        renderType, bufferSource, buffer,
                        isReRender, partialTick, packedLight, packedOverlay, colour);
            }

            poseStack.popPose();
            return; // 完整覆盖了默认逻辑，直接 return
        }

        // 默认流程
        buffer = checkAndRefreshBuffer(isReRender, buffer, bufferSource, renderType);
        renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);

        if (!isReRender)
            applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource,
                    buffer, partialTick, packedLight, packedOverlay);

        // 递归孩子
        renderChildBones(poseStack, animatable, bone, renderType, bufferSource,
                buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        poseStack.popPose();
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