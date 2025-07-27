package com.ankrya.doomgreats.item.model;

import com.ankrya.doomgreats.DoomGreats;
import com.ankrya.doomgreats.item.base.BaseGeoArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoArmorModel extends GeoModel<BaseGeoArmor> {
    @Override
    public ResourceLocation getModelResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, armor.getModel());
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, armor.getTexture());
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, armor.getAnimation());
    }
}
