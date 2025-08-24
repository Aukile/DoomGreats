package com.ankrya.doomsgreats.item.premise.model.base;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.interfaces.IGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;

public class BaseGeoItemModel<T extends Item & IGeoItem> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "geo/" + item.getModel() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/item/" + item.getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T item) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "animations/" + item.getModel() + ".animation.json");
    }
}
