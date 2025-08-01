package com.ankrya.doomsgreats.help.json;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
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
}
