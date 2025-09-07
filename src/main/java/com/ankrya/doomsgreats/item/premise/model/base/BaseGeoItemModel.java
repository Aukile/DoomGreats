package com.ankrya.doomsgreats.item.premise.model.base;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.interfaces.geo.IGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoItemModel<T extends Item & IGeoItem> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T item) {
        return GJ.Easy.getResource("geo/" + item.getModel() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T item) {
        return GJ.Easy.getResource("textures/item/" + item.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T item) {
        return GJ.Easy.getResource("animations/" + item.getModel() + ".animation.json");
    }
}
