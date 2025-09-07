package com.ankrya.doomsgreats.item.premise.renderer;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.item.items.weapon.GoldenGeatsBusterQB9;
import com.ankrya.doomsgreats.item.premise.renderer.base.BaseGeoItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class QB9Renderer extends BaseGeoItemRenderer<GoldenGeatsBusterQB9> {
    @Override
    public void actuallyRender(PoseStack poseStack, GoldenGeatsBusterQB9 animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(animatable, this));
        BakedGeoModel chargeModel = this.getGeoModel().getBakedModel(GJ.Easy.getResource("geo/dooms_geats_qb9_charge.geo.json"));
        RenderType chargeRenderType = RenderType.eyes(GJ.Easy.getResource("item/qb_9_charge_glowmask.png"));
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        super.actuallyRender(poseStack, animatable, chargeModel, chargeRenderType, bufferSource, buffer, true, partialTick, packedLight, packedOverlay, colour);
    }
}
