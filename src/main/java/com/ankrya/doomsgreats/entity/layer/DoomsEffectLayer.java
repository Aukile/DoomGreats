package com.ankrya.doomsgreats.entity.layer;

import com.ankrya.doomsgreats.client.render.RenderBase;
import com.ankrya.doomsgreats.entity.DoomsEffect;
import com.ankrya.doomsgreats.help.GJ;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class DoomsEffectLayer extends GeoRenderLayer<DoomsEffect> {
    private static final ResourceLocation LAYER = GJ.Easy.getResource("textures/entities/dooms_greatshenxin_e.png");
    public DoomsEffectLayer(GeoRenderer<DoomsEffect> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, DoomsEffect animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderBase.glowing(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, -1);
    }
}
