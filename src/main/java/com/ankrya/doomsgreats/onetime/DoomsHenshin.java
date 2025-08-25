package com.ankrya.doomsgreats.onetime;

import com.ankrya.doomsgreats.client.sound.SoundName;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.entity.DoomsEffect;
import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.help.runnable.WaitToRun;
import com.ankrya.doomsgreats.init.ClassRegister;
import com.ankrya.doomsgreats.interfaces.IGeoItem;
import com.ankrya.doomsgreats.item.items.armor.DesireDriver;
import com.ankrya.doomsgreats.item.items.armor.DoomsGreatsArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

/**好奇怪的写法，我为什么这么玩？ 不道哇*/
public class DoomsHenshin extends GJ {

    public DoomsHenshin(Player player) {
        super(player);
    }

    @Override
    public void use() {
        henshin();
    }

    public void henshin() {
        ItemStack driver = ToItem.getDriver(player);
        PlayerAnimator.playerAnimation(player, DesireDriver.REVOLVE, true);
        IGeoItem.playAnimationAndReset(driver, DesireDriver.REVOLVE);
        DesireDriver.setAnimationMode(driver, true);
        new WaitToRun(() -> {
            ToPlayer.stopSound(player, SoundName.BUCKLE_SET);
            ToPlayer.playSound(player, SoundName.REVOLVE_ON);
        },5);
        new WaitToRun(() -> {
            DesireDriver.setAnimationMode(driver, false);
            IGeoItem.playAnimationAndReset(driver, DesireDriver.IDLE);
        }, 84);
        new WaitToRun(() -> {
            Level world = player.level();
            ToPlayer.playSound(player, SoundName.HENSHIN);
            DoomsEffect effect = new DoomsEffect(world, player);
            effect.setPos(player.getX(), player.getY(), player.getZ());
            world.addFreshEntity(effect);
            new WaitToRun(() -> {
                DoomsGreatsArmor.equipOrRemoveAll(player);
                new WaitToRun(() -> {
                    IGeoItem.playAnimationAndReset(player.getItemBySlot(EquipmentSlot.CHEST), DoomsGreatsArmor.HENSHIN);
                    new WaitToRun(() -> {
                        IGeoItem.playAnimationAndReset(player.getItemBySlot(EquipmentSlot.CHEST), DoomsGreatsArmor.IDLE);
                        DoomsGreatsArmor.cloakEffect(player, true);
                    }, 317);
                }, 37);
            }, 177);
            new WaitToRun(() -> {
                ItemStack stack = new ItemStack(ClassRegister.getRegisterObject("buster_qb_9_sword", Item.class).get());
                ItemHandlerHelper.giveItemToPlayer(player, stack);
            }, 234);
        }, 28);
    }
}
