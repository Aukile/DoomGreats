package com.ankrya.doomsgreats.event;

import com.ankrya.doomsgreats.api.event.ArmorBrokenEvent;
import com.ankrya.doomsgreats.api.event.RiderArmorEquipEvent;
import com.ankrya.doomsgreats.api.event.RiderArmorRemoveEvent;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.items.armor.DesireDriver;
import com.ankrya.doomsgreats.item.items.armor.DoomsGreatsArmor;
import com.ankrya.doomsgreats.item.premise.base.armor.BaseDriver;
import com.ankrya.doomsgreats.item.premise.base.armor.BaseRiderArmor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * 灾厄极狐的普攻~
 */
@EventBusSubscriber
public class PlayerEvent {
    public static final String GREATS_HIT_COOLING = "greats_hit_cooling";
    public static final String GREATS_HIT_SEGMENT = "greats_hit_segment";
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        ItemStack driver = HTool.getDriver(event.getEntity());
        int hit = ItemHelp.getNbt(driver).getInt(GREATS_HIT_COOLING);
        if (driver.getItem() instanceof BaseDriver && hit > 0){
            ItemHelp.setNbt(driver, nbt -> nbt.putInt(GREATS_HIT_COOLING, hit - 1));
        }
    }

    @SubscribeEvent
    public static void onArmorEquip(RiderArmorEquipEvent.Pre event) {
        ItemStack driver = HTool.getDriver(event.getEntity());
        if (event.canRun() && driver.getItem() instanceof DesireDriver && !ItemHelp.getNbt(driver).getBoolean(DesireDriver.BUCKLE)){
            ItemHelp.setNbt(driver, nbt -> nbt.putBoolean(DesireDriver.BUCKLE, true));
        }
    }

    @SubscribeEvent
    public static void afterArmorEquip(RiderArmorEquipEvent event){
        LivingEntity entity = event.getEntity();
        if (entity.getHealth() < entity.getMaxHealth() && DoomsGreatsArmor.isAllEquip(entity)){
            entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1 , 9, false, false));
        }
    }

    @SubscribeEvent
    public static void onArmorUnequip(RiderArmorRemoveEvent.Pre event) {
        ItemStack driver = HTool.getDriver(event.getEntity());
        if (event.canRun() && driver.getItem() instanceof DesireDriver && ItemHelp.getNbt(driver).getBoolean(DesireDriver.BUCKLE)){
            ItemHelp.setNbt(driver, nbt -> nbt.putBoolean(DesireDriver.BUCKLE, false));
        }
    }

    @SubscribeEvent
    public static void afterArmorUnequip(RiderArmorRemoveEvent event){
        LivingEntity entity = event.getEntity();
        HTool.fixHealth(entity);
    }

    @SubscribeEvent
    public static void onArmorBroken(ArmorBrokenEvent event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() instanceof BaseRiderArmor armor && event.getEntity() instanceof Player player) {
            ItemStack backupArmor = BaseRiderArmor.getBackupArmor(stack);
            player.setItemSlot(armor.getSlot(), backupArmor);
        }
    }

    /**
     * 本来想放在{@link net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty} 里面的 <br>
     * 然后发现它攻击实体并不触发。。。 <br>
     * <p>
     * @see com.ankrya.doomsgreats.mixin.SwordItemMixin
     */
    public static void hit(ItemStack stack, Player entity, Level world, int time){
        ItemHelp.setNbt(stack, nbt -> nbt.putInt(GREATS_HIT_SEGMENT, time > 2 ? 0 : time + 1));
        ItemHelp.setNbt(stack, nbt -> nbt.putInt(GREATS_HIT_COOLING, 20));
        PlayerAnimator.playerAnimation(entity, "attack" + (time + 1), true);
        if (time == 3){
            for (Entity target : HTool.rangeFind(entity, 8)) {
                if (entity != target && HTool.isFront(entity, target, 0)) {
                    HTool.ExplosionTo(entity, target, world, 50);
                    target.setDeltaMovement(new Vec3((entity.getLookAngle().x * 4), (entity.getLookAngle().y * -1), (entity.getLookAngle().z * 4)));
                }
            }
        }
    }
}
