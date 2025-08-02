package com.ankrya.doomsgreats.event;

import com.ankrya.doomsgreats.help.json.ItemHelp;
import com.ankrya.doomsgreats.item.base.BaseDriver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class PlayerEvent {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player entity = event.getEntity();
        ItemStack driver = entity.getItemBySlot(EquipmentSlot.LEGS);
        int hit = ItemHelp.getNbt(driver).getInt("greats_hit");
        if (driver.getItem() instanceof BaseDriver && hit > 0){
            ItemHelp.setNbt(driver, nbt -> nbt.putInt("greats_hit", hit - 1));
        }
    }
}
