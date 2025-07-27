package com.ankrya.doomgreats.item.model;

import com.ankrya.doomgreats.DoomGreats;
import com.ankrya.doomgreats.item.base.BaseGeoItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoItemModel extends GeoModel<BaseGeoItem> {
    @Override
    public ResourceLocation getModelResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, item.getModel());
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, item.getTexture());
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoItem item) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, item.getAnimation());
    }
}
