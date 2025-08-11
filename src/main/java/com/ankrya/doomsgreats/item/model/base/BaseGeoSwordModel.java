package com.ankrya.doomsgreats.item.model.base;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.item.base.BaseGeoSword;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoSwordModel extends GeoModel<BaseGeoSword> {
    @Override
    public ResourceLocation getModelResource(BaseGeoSword item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "geo/" + item.getModel() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoSword item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/item/" + item.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoSword item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "animations/" + item.getModel() + ".animation.json");
    }
}
