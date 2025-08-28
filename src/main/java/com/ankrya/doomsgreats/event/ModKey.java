package com.ankrya.doomsgreats.event;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.item.items.armor.DesireDriver;
import com.ankrya.doomsgreats.item.items.armor.DoomsGreatsArmor;
import com.ankrya.doomsgreats.message.MessageLoader;
import com.ankrya.doomsgreats.message.NMessageCreater;
import com.ankrya.doomsgreats.message.ex_message.AllPackt;
import com.ankrya.doomsgreats.message.ex_message.OnetimeMessage;
import com.ankrya.doomsgreats.onetime.DoomsHenshin;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(Dist.CLIENT)
public class ModKey {
    public static final Lazy<KeyMapping> HENSHIN = Lazy.of(new KeyMapping("key.dooms_greats.henshin", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.dooms_greats"));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(HENSHIN.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (HENSHIN.get().consumeClick()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && GJ.ToItem.getDriver(player).getItem() instanceof DesireDriver
                    && !DoomsGreatsArmor.isAllEquip(player)
                    && GJ.ToItem.getNbt(GJ.ToItem.getDriver(player))
                    .getString(DesireDriver.ANIMATION).equals(DesireDriver.IDLE0)){
                MessageLoader.sendToServer(new NMessageCreater(new AllPackt(new OnetimeMessage(new DoomsHenshin(player)))));
                System.out.println("--click--");
            }
        }
    }
}
