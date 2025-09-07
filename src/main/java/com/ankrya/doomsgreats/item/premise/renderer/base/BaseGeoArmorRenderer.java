package com.ankrya.doomsgreats.item.premise.renderer.base;

import com.ankrya.doomsgreats.interfaces.geo.IGeoArmor;
import com.ankrya.doomsgreats.interfaces.geo.IGeoItem;
import com.ankrya.doomsgreats.item.premise.base.armor.BaseGeoArmor;
import com.ankrya.doomsgreats.item.premise.model.base.BaseGeoArmorModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Map;

public class BaseGeoArmorRenderer<T extends BaseGeoArmor> extends GeoArmorRenderer<T>{
    private IGeoItem item;
    public BaseGeoArmorRenderer() {
        super(new BaseGeoArmorModel<>());
    }

    @SuppressWarnings("unchecked")
    public BaseGeoArmorRenderer(IGeoItem item) {
        this();
        this.item = item;
        for(GeoRenderLayer<?> layer :item.getRenderLayers(this))
            this.addRenderLayer((GeoRenderLayer<T>) layer);
        if (item.autoGlow()) this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.getRenderType(texture) == null) return super.getRenderType(animatable, texture, bufferSource, partialTick);
        return animatable.getRenderType(texture);
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        if (getGeoArmorInterface()!= null) {
            getGeoArmorInterface().visibilityBones(this, currentSlot);
        }
        if (getGeoArmorInterface() != null){
            Map<String, Boolean> set = getGeoArmorInterface().visibilityBones(this);
            if (!set.isEmpty()) set.forEach((boneName, visible) -> this.model.getBone(boneName).ifPresent(bone -> bone.setHidden(visible)));
        }
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);
        if (getGeoArmorInterface() != null)
            getGeoArmorInterface().transformations(this);
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        if (getGeoArmorInterface() != null && !getGeoArmorInterface().lightBones(this).isEmpty()){
            MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
            String name = bone.getName();
            if (getGeoArmorInterface().lightBones(this).contains(name)) {
                VertexConsumer newBuffer = source.getBuffer(RenderType.beaconBeam(getTextureLocation(animatable), false));
                super.renderCubesOfBone(poseStack, bone, newBuffer, packedLight, packedOverlay, colour);
            } else super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
        } else super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
    }

    private IGeoItem getGeoItemBaseInterface() {
        return item == null ? this.getAnimatable() : item;
    }

    private IGeoArmor getGeoArmorInterface(){
        if (getGeoItemBaseInterface() instanceof IGeoArmor geoArmor)
            return geoArmor;
        return null;
    }
}
