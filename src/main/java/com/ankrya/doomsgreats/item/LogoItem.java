package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.data.ModVariable;
import com.ankrya.doomsgreats.data.Variables;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LogoItem extends Item {
    public LogoItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> use = super.use(level, player, usedHand);

        ItemStack stack = use.getObject();
        Variables data = player.getData(Variables.VARIABLES);
        int value = data.getVariable(ModVariable.test);
        if (player.isShiftKeyDown()){
            if (!level.isClientSide)
                player.displayClientMessage(Component.literal("数值为：" + value), true);
        } else{
            player.displayClientMessage(Component.literal("现在是：" + value + "+1" + (level.isClientSide ? "/客户端" : "/服务器")), false);
            player.getCooldowns().addCooldown(stack.getItem(), 5);
            data.setVariable(player, ModVariable.test, value + 1);
        }
        return use;

    }
}
