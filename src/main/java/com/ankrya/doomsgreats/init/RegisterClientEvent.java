package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.client.particle.base.AdvancedParticleBase;
import com.ankrya.doomsgreats.client.particle.base.ParticleRibbon;
import com.ankrya.doomsgreats.client.particle.base.SpreadBase;
import com.ankrya.doomsgreats.client.particle.base.advanced.AdvancedParticleData;
import com.ankrya.doomsgreats.client.particle.base.advanced.RibbonParticleData;
import com.ankrya.doomsgreats.client.particle.provider.KatanaSlashParticleProvider;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.entity.renderer.DoomsEffectRenderer;
import com.ankrya.doomsgreats.entity.renderer.SpecialEffectRenderer;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.items.weapon.GoldenGeatsBusterQB9;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class RegisterClientEvent {

    @SubscribeEvent
    public static void registerPlayerAnimator(final FMLClientSetupEvent event) {
        PlayerAnimator.init();

        event.enqueueWork(()->{
            ItemProperties.register(ClassRegister.getRegisterObject("buster_qb_9_sword", Item.class).get(),ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID,"qb9_mode"),(itemStack, clientWorld, entity, itemEntityId)-> ItemHelp.getNbt(itemStack).getInt(GoldenGeatsBusterQB9.QB9_MODE));
        });
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ClassRegister.getRegisterObject("henshin_effect", EntityType.class).get(), context -> new SpecialEffectRenderer<>(context));
        event.registerEntityRenderer(ClassRegister.getRegisterObject("dooms_effect", EntityType.class).get(), DoomsEffectRenderer::new);
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ClassRegister.getRegisterObject("katana_slash", ParticleType.class).get(), KatanaSlashParticleProvider::new);
        event.registerSpriteSet(AdvancedParticleData.getParticleType(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(RibbonParticleData.getRibbonParticleType(), ParticleRibbon.Factory::new);
        event.registerSpriteSet(ClassRegister.getRegisterObject("case_spread", ParticleType.class).get(), SpreadBase.CaseSpreadProvider::new);
    }
}
