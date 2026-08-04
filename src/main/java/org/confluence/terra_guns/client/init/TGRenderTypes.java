package org.confluence.terra_guns.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.client.renderer.RenderStateShard.*;

public final class TGRenderTypes {
    private static final Map<String, RenderType> TRAILS = new ConcurrentHashMap<>();
    private static final RenderType CONFETTI = RenderType.create(
            "terra_guns_confetti",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(POSITION_COLOR_SHADER)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setWriteMaskState(COLOR_WRITE)
                    .setOutputState(TRANSLUCENT_TARGET)
                    .createCompositeState(false)
    );

    private TGRenderTypes() {
    }

    public static RenderType trail(ResourceLocation texture, boolean additive) {
        String key = texture + (additive ? ":additive" : ":translucent");
        return TRAILS.computeIfAbsent(key, ignored -> RenderType.create(
                "terra_guns_trail_" + Integer.toHexString(key.hashCode()),
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(additive ? ADDITIVE_TRANSPARENCY : TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setWriteMaskState(COLOR_WRITE)
                        .setOutputState(TRANSLUCENT_TARGET)
                        .createCompositeState(false)
        ));
    }

    public static RenderType confetti() {
        return CONFETTI;
    }
}
