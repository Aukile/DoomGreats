package com.ankrya.doomsgreats.entity.model;

import com.ankrya.doomsgreats.entity.SpecialEffectEntity;
import com.ankrya.doomsgreats.help.GJ;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpecialEffectModel<T extends SpecialEffectEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(SpecialEffectEntity specialEffectEntity) {
        return GJ.Easy.getResource("geo/"+ specialEffectEntity.model()+".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpecialEffectEntity specialEffectEntity) {
        return GJ.Easy.getResource("textures/entities/"+ specialEffectEntity.texture()+".png");
    }

    @Override
    public ResourceLocation getAnimationResource(SpecialEffectEntity specialEffectEntity) {
        return GJ.Easy.getResource("animations/"+ specialEffectEntity.model()+".animation.json");
    }
}
