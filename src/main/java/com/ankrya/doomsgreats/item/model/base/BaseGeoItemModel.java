package com.ankrya.doomsgreats.item.model.base;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.item.base.BaseGeoItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoItemModel extends GeoModel<BaseGeoItem> {
    @Override
    public ResourceLocation getModelResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, item.getModel());
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, item.getTexture());
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, item.getAnimation());
    }
}
