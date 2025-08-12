package com.ankrya.doomsgreats.event;

import com.ankrya.doomsgreats.api.event.ArmorBrokenEvent;
import com.ankrya.doomsgreats.api.event.RiderArmorEquipEvent;
import com.ankrya.doomsgreats.api.event.RiderArmorRemoveEvent;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.DesireDriver;
import com.ankrya.doomsgreats.item.DoomsGreatsArmor;
import com.ankrya.doomsgreats.item.base.armor.BaseDriver;
import com.ankrya.doomsgreats.item.base.armor.BaseRiderArmor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@EventBusSubscriber
public class PlayerEvent {
    public static final String GREATS_HIT_COOLING = "greats_hit_cooling";
    public static final String GREATS_HIT_SEGMENT = "greats_hit_segment";
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player entity = event.getEntity();
        ItemStack driver = entity.getItemBySlot(EquipmentSlot.LEGS);
        int hit = ItemHelp.getNbt(driver).getInt(GREATS_HIT_COOLING);
        if (driver.getItem() instanceof BaseDriver && hit > 0){
            ItemHelp.setNbt(driver, nbt -> nbt.putInt(GREATS_HIT_COOLING, hit - 1));
        }
    }

    @SubscribeEvent
    public static void onArmorEquip(RiderArmorEquipEvent event) {
        ItemStack driver = event.getEntity().getItemBySlot(EquipmentSlot.LEGS);
        if (event.canRun() && driver.getItem() instanceof DesireDriver && !ItemHelp.getNbt(driver).getBoolean(DesireDriver.BUCKLE)){
            ItemHelp.setNbt(driver, nbt -> nbt.putBoolean(DesireDriver.BUCKLE, true));
        }
    }

    @SubscribeEvent
    public static void onArmorUnequip(RiderArmorRemoveEvent event) {
        ItemStack driver = event.getEntity().getItemBySlot(EquipmentSlot.LEGS);
        if (event.canRun() && driver.getItem() instanceof DesireDriver && ItemHelp.getNbt(driver).getBoolean(DesireDriver.BUCKLE)){
            ItemHelp.setNbt(driver, nbt -> nbt.putBoolean(DesireDriver.BUCKLE, false));
        }
    }

    @SubscribeEvent
    public static void onArmorBroken(ArmorBrokenEvent event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() instanceof BaseRiderArmor armor && event.getEntity() instanceof Player player) {
            ItemStack backupArmor = BaseRiderArmor.getBackupArmor(stack);
            player.setItemSlot(armor.getSlot(), backupArmor);
        }
    }

//    @SubscribeEvent
//    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event){
//        Player entity = event.getEntity();
//        Level level = event.getLevel();
//        ItemStack driver = entity.getItemBySlot(EquipmentSlot.LEGS);
//        if (DoomsGreatsArmor.isAllEquip(entity)
//                && entity.getMainHandItem().getItem() instanceof SwordItem){
//            int time = ItemHelp.getNbt(driver).getInt(GREATS_HIT_SEGMENT);
//            hit(driver, entity, level, time);
//        }
//    }

    public static void hit(ItemStack stack, Player entity, Level world, int time){
        ItemHelp.setNbt(stack, nbt -> nbt.putInt(GREATS_HIT_SEGMENT, time > 2 ? 0 : time + 1));
        ItemHelp.setNbt(stack, nbt -> nbt.putInt(GREATS_HIT_COOLING, 20));
        PlayerAnimator.playerAnimation(entity, "attack" + (time + 1), true);
        if (time == 3){
            for (Entity target : HTool.rangeFind(entity, 8)) {
                if (entity != target) {
                    if (HTool.isFront(entity, target, 0)) {
                        HTool.ExplosionTo(entity, target, world);
                        target.setDeltaMovement(new Vec3((target.getLookAngle().x * -4), (target.getLookAngle().y * -1), (target.getLookAngle().z * -4)));
                    }
                }
            }
        }
    }
}
