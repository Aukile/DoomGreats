package com.ankrya.doomsgreats.entity.renderer;

import com.ankrya.doomsgreats.entity.SpecialEffect;
import com.ankrya.doomsgreats.entity.model.SpecialEffectModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpecialEffectRenderer extends GeoEntityRenderer<SpecialEffect> {
    public SpecialEffectRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpecialEffectModel());
    }
}
