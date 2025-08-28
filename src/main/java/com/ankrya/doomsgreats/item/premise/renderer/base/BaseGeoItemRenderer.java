package com.ankrya.doomsgreats.item.premise.renderer.base;

import com.ankrya.doomsgreats.interfaces.IGeoItem;
import com.ankrya.doomsgreats.item.premise.model.base.BaseGeoItemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BaseGeoItemRenderer<T extends Item & IGeoItem> extends GeoItemRenderer<T> {
    public BaseGeoItemRenderer() {
        super(new BaseGeoItemModel<>());
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.getRenderType(texture) == null) return super.getRenderType(animatable, texture, bufferSource, partialTick);
        return animatable.getRenderType(texture);
    }
}
