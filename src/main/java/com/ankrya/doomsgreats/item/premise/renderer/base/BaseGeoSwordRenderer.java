package com.ankrya.doomsgreats.item.premise.renderer.base;

import com.ankrya.doomsgreats.item.premise.base.BaseGeoSword;
import com.ankrya.doomsgreats.item.premise.model.base.BaseGeoSwordModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BaseGeoSwordRenderer extends GeoItemRenderer<BaseGeoSword> {
    public BaseGeoSwordRenderer() {
        super(new BaseGeoSwordModel());
    }

    @Override
    public @Nullable RenderType getRenderType(BaseGeoSword animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.getRenderType(texture) == null) return super.getRenderType(animatable, texture, bufferSource, partialTick);
        return animatable.getRenderType(texture);
    }
}
