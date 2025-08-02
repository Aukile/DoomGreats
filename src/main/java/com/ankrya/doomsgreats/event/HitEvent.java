package com.ankrya.doomsgreats.event;

import com.ankrya.doomsgreats.help.Mtb;
import com.ankrya.doomsgreats.help.json.ItemHelp;
import com.ankrya.doomsgreats.item.base.BaseDriver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber
public class HitEvent {

    @SubscribeEvent
    public static void onHit(InputEvent.InteractionKeyMappingTriggered event) {
        Player player = Mtb.getLocalPlayer();
        if (player == null) return;
        ItemStack driver = player.getItemBySlot(EquipmentSlot.LEGS);
        if (driver.getItem() instanceof BaseDriver && ItemHelp.getNbt(driver).getInt("greats_hit") > 0) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }
}