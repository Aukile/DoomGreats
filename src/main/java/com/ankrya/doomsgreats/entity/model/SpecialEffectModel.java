package com.ankrya.doomsgreats.entity.model;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.entity.SpecialEffect;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpecialEffectModel extends GeoModel<SpecialEffect> {
    @Override
    public ResourceLocation getModelResource(SpecialEffect specialEffect) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "geo/"+specialEffect.model()+".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpecialEffect specialEffect) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/entity/"+specialEffect.texture()+".png");
    }

    @Override
    public ResourceLocation getAnimationResource(SpecialEffect specialEffect) {
        return ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "animations/"+specialEffect.model()+".animation.json");
    }
}
