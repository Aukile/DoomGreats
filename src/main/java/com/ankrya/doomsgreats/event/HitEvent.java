package com.ankrya.doomsgreats.event;

import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.premise.base.armor.BaseDriver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class HitEvent {

    @SubscribeEvent
    public static void onHit(InputEvent.InteractionKeyMappingTriggered event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null){
            ItemStack driver = HTool.getDriver(player);
            if (driver.getItem() instanceof BaseDriver
                    && ItemHelp.getNbt(driver).getInt(PlayerEvent.GREATS_HIT_COOLING) > 0) {
                event.setSwingHand(false);
                event.setCanceled(true);
            }
        }
    }
}