package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.entity.renderer.SpecialEffectRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class RegisterClientEvent {

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ClassRegister.getRegisterObject("henshin_effect", EntityType.class).get(), SpecialEffectRenderer::new);
    }
}
