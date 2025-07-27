package com.ankrya.doomgreats.entity.renderer;

import com.ankrya.doomgreats.entity.SpecialEffect;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpecialEffectRenderer extends GeoEntityRenderer<SpecialEffect> {
    public SpecialEffectRenderer(EntityRendererProvider.Context renderManager, GeoModel<SpecialEffect> model) {
        super(renderManager, model);
    }
}
