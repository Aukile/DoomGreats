package com.ankrya.doomsgreats.item.model.base;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.item.base.BaseGeoItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoItemModel extends GeoModel<BaseGeoItem> {
    @Override
    public ResourceLocation getModelResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "geo/" + item.getModel() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/item/" + item.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "animations/" + item.getModel() + ".animation.json");
    }
}
