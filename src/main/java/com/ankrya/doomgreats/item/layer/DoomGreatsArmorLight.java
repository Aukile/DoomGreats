package com.ankrya.doomgreats.item.layer;

import com.ankrya.doomgreats.DoomGreats;
import com.ankrya.doomgreats.item.DoomGreatsArmor;
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

public class DoomGreatsArmorLight extends GeoRenderLayer<DoomGreatsArmor> {
    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "textures/items/dooms_greats_e.png");
    public DoomGreatsArmorLight(GeoRenderer<DoomGreatsArmor> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, DoomGreatsArmor animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1);
    }
}
