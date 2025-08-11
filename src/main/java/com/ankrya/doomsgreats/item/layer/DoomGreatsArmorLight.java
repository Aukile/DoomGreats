package com.ankrya.doomsgreats.item.layer;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.item.DoomsGreatsArmor;
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
import software.bernie.geckolib.util.Color;

public class DoomGreatsArmorLight extends GeoRenderLayer<DoomsGreatsArmor> {
    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/item/dooms_greats_glowmask.png");
    public DoomGreatsArmorLight(GeoRenderer<DoomsGreatsArmor> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, DoomsGreatsArmor animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(glowRenderType);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, vertexConsumer, partialTick, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFF0000);
    }
}
