package com.ankrya.doomsgreats.item.premise.model.base;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.item.premise.base.BaseGeoSword;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoSwordModel extends GeoModel<BaseGeoSword> {
    @Override
    public ResourceLocation getModelResource(BaseGeoSword item) {
        return GJ.Easy.getResource("geo/" + item.getModel() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BaseGeoSword item) {
        return GJ.Easy.getResource("textures/item/" + item.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BaseGeoSword item) {
        return GJ.Easy.getResource("animations/" + item.getModel() + ".animation.json");
    }
}
