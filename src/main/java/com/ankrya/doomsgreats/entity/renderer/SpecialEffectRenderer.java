package com.ankrya.doomsgreats.entity.renderer;

import com.ankrya.doomsgreats.entity.SpecialEffect;
import com.ankrya.doomsgreats.entity.model.SpecialEffectModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpecialEffectRenderer<T extends SpecialEffect> extends GeoEntityRenderer<T> {
    public SpecialEffectRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpecialEffectModel<>());
    }
}
