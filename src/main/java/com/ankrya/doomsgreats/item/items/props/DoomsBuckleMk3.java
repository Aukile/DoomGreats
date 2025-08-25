package com.ankrya.doomsgreats.item.items.props;

import com.ankrya.doomsgreats.client.sound.SoundName;
import com.ankrya.doomsgreats.compat.animation.AnimName;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.help.runnable.WaitToRun;
import com.ankrya.doomsgreats.init.ClassRegister;
import com.ankrya.doomsgreats.item.premise.base.BaseGeoItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DoomsBuckleMk3 extends BaseGeoItem {
    public DoomsBuckleMk3(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if (usedHand == InteractionHand.MAIN_HAND && player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()){
            Item doomsMk9Left = ClassRegister.getRegisterObject("dooms_mk_9_left", Item.class).get();
            ItemStack mk9Stack = new ItemStack(doomsMk9Left);
            PlayerAnimator.playerAnimation(player, AnimName.BUCKLE_OPEN, true);
            GJ.ToPlayer.playSound(player, SoundName.BUCKLE_OPEN);
            GJ.ToPlayer.playDelaySound(player, SoundName.BUCKLE_OPEN_WAIT, true, 8);
            new WaitToRun(() -> {
                player.setItemSlot(EquipmentSlot.MAINHAND, mk9Stack);
                player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ClassRegister.getRegisterObject("dooms_mk_9_right", Item.class).get()));
            }, 8);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public String getModel() {
        return "dooms_geats_buckle_mk3";
    }

    @Override
    public String getTexture() {
        return "dooms_geats_belt";
    }
}
