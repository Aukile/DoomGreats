package com.ankrya.doomgreats.entity.model;

import com.ankrya.doomgreats.DoomGreats;
import com.ankrya.doomgreats.entity.SpecialEffect;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpecialEffectModel extends GeoModel<SpecialEffect> {
    @Override
    public ResourceLocation getModelResource(SpecialEffect specialEffect) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "geo/"+specialEffect.model()+".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpecialEffect specialEffect) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "textures/entity/"+specialEffect.texture()+".png");
    }

    @Override
    public ResourceLocation getAnimationResource(SpecialEffect specialEffect) {
        return ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "animations/"+specialEffect.model()+".animation.json");
    }
}
