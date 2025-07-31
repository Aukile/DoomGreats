package com.ankrya.doomsgreats.item.renderer;

import com.ankrya.doomsgreats.item.base.BaseGeoItem;
import com.ankrya.doomsgreats.item.model.BaseGeoItemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BaseGeoItemRenderer extends GeoItemRenderer<BaseGeoItem> {
    public BaseGeoItemRenderer() {
        super(new BaseGeoItemModel());
    }

    @Override
    public @Nullable RenderType getRenderType(BaseGeoItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.getRenderType() == null) return super.getRenderType(animatable, texture, bufferSource, partialTick);
        return animatable.getRenderType();
    }
}
