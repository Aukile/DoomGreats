package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.entity.SpecialEffect;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber
public class RegisterEvent {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ClassRegister.getRegisterObject("henshin_effect", EntityType.class).get(), SpecialEffect.createAttributes().build());
        event.put(ClassRegister.getRegisterObject("dooms_effect", EntityType.class).get(), SpecialEffect.createAttributes().build());
    }

}
