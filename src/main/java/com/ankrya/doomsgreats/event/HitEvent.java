package com.ankrya.doomsgreats.event;

import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.base.armor.BaseDriver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class HitEvent {

    @SubscribeEvent
    public static void onHit(InputEvent.InteractionKeyMappingTriggered event) {
        HTool.getLocalPlayer().ifPresent(player -> {
            ItemStack driver = player.getItemBySlot(EquipmentSlot.LEGS);
            if (driver.getItem() instanceof BaseDriver
                    && ItemHelp.getNbt(driver).getInt(PlayerEvent.GREATS_HIT_COOLING) > 0) {
                event.setSwingHand(false);
                event.setCanceled(true);
            }
        });
    }
}