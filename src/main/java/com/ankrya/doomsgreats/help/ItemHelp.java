package com.ankrya.doomsgreats.help;

import com.ankrya.doomsgreats.init.ClassRegister;
import com.ankrya.doomsgreats.interfaces.IFMessage;
import com.ankrya.doomsgreats.message.MessageCreater;
import com.ankrya.doomsgreats.message.MessageLoader;
import com.ankrya.doomsgreats.message.ex_message.EquipmentMessage;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ItemHelp {
    public static final String REMOVE = "dooms_remove";
    public static void setNbt(ItemStack itemStack, Consumer<CompoundTag> updater){
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, updater);
    }

    public static CompoundTag getNbt(ItemStack itemStack){
        return itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void equipBySlot(Entity entity, EquipmentSlot slot, ItemStack stack){
        if (entity instanceof Player player) {
            player.getInventory().armor.set(slot.getIndex(), stack);
            player.getInventory().setChanged();
            List<Pair<EquipmentSlot, ItemStack>> slots = List.of(Pair.of(slot, stack));
            if (player instanceof ServerPlayer serverPlayer)
                serverPlayer.connection.send(new ClientboundSetEquipmentPacket(player.getId(), slots));
        } else if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setItemSlot(slot, stack);
        }
    }

    public static void playerRemoveItem(Player player, Item item, int count){
        playerRemoveItem(player, itemStack -> itemStack.is(item), count);
    }

    public static void playerRemoveItem(Player player, ItemStack stack, int count){
        if (!getNbt(stack).getBoolean(REMOVE))
            setNbt(stack, tag -> tag.putBoolean(REMOVE, true));
        playerRemoveItem(player, itemStack -> itemStack == stack, count);
    }

    public static void playerRemoveItem(Player player, Predicate<ItemStack> condition, int count){
        player.getInventory().clearOrCountMatchingItems(condition, count, player.getInventory());
    }

    public static boolean checkItem(ItemStack stack, String registerName){
        return stack.is(ClassRegister.getRegisterObject(registerName, Item.class).get());
    }
}
