package com.ankrya.doomsgreats.item.model.base;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.item.base.armor.BaseGeoArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoArmorModel<T extends BaseGeoArmor> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "geo/" + armor.getModel() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/item/" + armor.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "animations/" + armor.getModel() + ".animation.json");
    }
}
