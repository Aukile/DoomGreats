package com.ankrya.doomgreats.item.model;

import com.ankrya.doomgreats.DoomGreats;
import com.ankrya.doomgreats.item.base.BaseGeoArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoArmorModel<T extends BaseGeoArmor> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "geo/" + armor.getModel() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "textures/items/" + armor.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoArmor armor) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "animations/" + armor.getModel() + ".animation.json");
    }
}
