package com.ankrya.doomsgreats.item.renderer;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.item.DoomsGreatsArmor;
import com.ankrya.doomsgreats.item.renderer.base.BaseRiderArmorRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;

public class DoomGreatsArmorRenderer extends BaseRiderArmorRender<DoomsGreatsArmor> {
    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/item/dooms_greats.png");
    public DoomGreatsArmorRenderer() {
        super();
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
        String name = bone.getName();
        if (name.equals("left_eye") || name.equals("right_eye")) {
            VertexConsumer newBuffer = source.getBuffer(RenderType.beaconBeam(LAYER, false));
            super.renderCubesOfBone(poseStack, bone, newBuffer, packedLight, packedOverlay, colour);
        } else super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
    }
}
