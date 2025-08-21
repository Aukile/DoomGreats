package com.ankrya.doomsgreats.message.ex_message;

import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.interfaces.IFMessage;
import com.ankrya.doomsgreats.item.base.armor.BaseRiderArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EquipmentMessage implements IFMessage {
    @Override
    public void run(IPayloadContext ctx) {
        Player player = ctx.player();

        for (EquipmentSlot slot : BaseRiderArmor.getSlots()){
            ItemHelp.equipBySlot(player, slot, player.getItemBySlot(slot));
        }
    }
}
