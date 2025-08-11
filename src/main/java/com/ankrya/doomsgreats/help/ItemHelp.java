package com.ankrya.doomsgreats.help;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.function.Consumer;

public final class ItemHelp {
    public static void setNbt(ItemStack itemStack, Consumer<CompoundTag> updater){
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, updater);
    }

    public static CompoundTag getNbt(ItemStack itemStack){
        return itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void playerBySlot(Player player, EquipmentSlot slot, ItemStack stack){
        player.getInventory().armor.set(slot.getIndex(), stack);
        player.getInventory().setChanged();
    }

    public static void playerRemoveItem(Player player, Item item, int count){
        player.getInventory().clearOrCountMatchingItems(itemStack -> itemStack.is(item), count, player.getInventory());
    }
}
