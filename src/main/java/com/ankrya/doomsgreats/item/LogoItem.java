package com.ankrya.doomsgreats.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/*
 * 图标物品真的是图标物品吗
 * 这里不是绝佳的测试处
 * go go go
 */
public class LogoItem extends Item {
    public LogoItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> use = super.use(level, player, usedHand);

        ItemStack stack = use.getObject();
        return use;

    }
}
