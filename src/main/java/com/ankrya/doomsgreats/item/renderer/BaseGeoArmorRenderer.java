package com.ankrya.doomsgreats.item.renderer;

import com.ankrya.doomsgreats.item.base.BaseGeoArmor;
import com.ankrya.doomsgreats.item.model.BaseGeoArmorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BaseGeoArmorRenderer<T extends BaseGeoArmor> extends GeoArmorRenderer<T> {
    public BaseGeoArmorRenderer() {
        super(new BaseGeoArmorModel<>());
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.getRenderType() == null) return super.getRenderType(animatable, texture, bufferSource, partialTick);
        return animatable.getRenderType();
    }
}
