package com.ankrya.doomsgreats.item.items.props;

import com.ankrya.doomsgreats.client.sound.SoundName;
import com.ankrya.doomsgreats.compat.animation.AnimName;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.help.runnable.WaitToRun;
import com.ankrya.doomsgreats.interfaces.IGeoItem;
import com.ankrya.doomsgreats.item.items.armor.DesireDriver;
import com.ankrya.doomsgreats.item.premise.base.EasyGeoItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DoomsBuckleMk9 extends EasyGeoItem{
    public DoomsBuckleMk9(Properties properties, String model, String texture) {
        super(properties, model, texture);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack driver = HTool.getDriver(player);
        ItemStack mainHandItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHandItem = player.getItemBySlot(EquipmentSlot.OFFHAND);
        if (ItemHelp.checkItem(mainHandItem, "dooms_mk_9_left")
                && ItemHelp.checkItem(offHandItem, "dooms_mk_9_right")
                && ItemHelp.checkItem(driver, "desire_driver")
                && !ItemHelp.getNbt(driver).getBoolean(DesireDriver.BUCKLE)){
            PlayerAnimator.playerAnimation(player, AnimName.BUCKLE_ON, true);
            new WaitToRun(() -> {
                ItemHelp.playerRemoveItem(player, mainHandItem, 1);
                ItemHelp.playerRemoveItem(player, offHandItem, 1);
                HTool.stopSound(player, SoundName.BUCKLE_OPEN);
                HTool.cancelDelaySound(player, SoundName.BUCKLE_OPEN_WAIT);

                HTool.playSound(player, SoundName.BUCKLE_SET);
                IGeoItem.playAnimationAndReset(driver, DesireDriver.IDLE0);
                ItemHelp.setNbt(driver, nbt -> nbt.putBoolean(DesireDriver.BUCKLE, true));
            }, 4);
        }
        return super.use(level, player, usedHand);
    }
}
