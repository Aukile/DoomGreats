package com.ankrya.doomsgreats.mixin;

import com.ankrya.doomsgreats.api.event.ArmorBrokenEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 1))
    public void hurtAndBreak(int damage, ServerLevel level, @Nullable LivingEntity entity, Consumer<Item> itemConsumer, CallbackInfo ci) {
        ItemStack itemStack = (ItemStack) (Object) this;
        NeoForge.EVENT_BUS.post(new ArmorBrokenEvent(entity, itemStack));
    }
}
