package com.ankrya.doomsgreats.mixin;

import com.ankrya.doomsgreats.event.PlayerEvent;
import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.DoomsGreatsArmor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin implements IItemExtension {

    @Unique
    @Override
    public boolean onEntitySwing(ItemStack stack, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (stack.getItem() instanceof SwordItem && entity instanceof Player player && DoomsGreatsArmor.isAllEquip(entity)){
            ItemStack driver = HTool.getDriver(entity);
            int time = ItemHelp.getNbt(driver).getInt(PlayerEvent.GREATS_HIT_SEGMENT);
            PlayerEvent.hit(driver, player, entity.level(), time);
            if (time == 3){
                return false;
            }
        }
        return IItemExtension.super.onEntitySwing(stack, entity, hand);
    }

    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true)
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof SwordItem && attacker instanceof Player && DoomsGreatsArmor.isAllEquip(attacker)){
            ItemStack driver = HTool.getDriver(attacker);
            int time = ItemHelp.getNbt(driver).getInt(PlayerEvent.GREATS_HIT_SEGMENT);
            if (time == 3){
                cir.setReturnValue(false);
            }
        }
    }
}
