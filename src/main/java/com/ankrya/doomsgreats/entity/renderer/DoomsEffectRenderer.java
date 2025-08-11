package com.ankrya.doomsgreats.entity.renderer;

import com.ankrya.doomsgreats.entity.DoomsEffect;
import com.ankrya.doomsgreats.entity.layer.DoomsEffectLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DoomsEffectRenderer extends SpecialEffectRenderer<DoomsEffect>{
    public DoomsEffectRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.addRenderLayer(new DoomsEffectLayer(this));
    }
}
