package com.ankrya.doomsgreats.item.renderer.base;

import com.ankrya.doomsgreats.item.base.BaseGeoSword;
import com.ankrya.doomsgreats.item.model.base.BaseGeoSwordModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BaseGeoSwordRenderer extends GeoItemRenderer<BaseGeoSword> {
    public BaseGeoSwordRenderer() {
        super(new BaseGeoSwordModel());
    }

    @Override
    public @Nullable RenderType getRenderType(BaseGeoSword animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.getRenderType() == null) return super.getRenderType(animatable, texture, bufferSource, partialTick);
        return animatable.getRenderType();
    }
}
