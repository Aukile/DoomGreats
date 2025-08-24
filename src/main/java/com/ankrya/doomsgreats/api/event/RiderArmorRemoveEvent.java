package com.ankrya.doomsgreats.api.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;

/**
 * 使用{@link com.ankrya.doomsgreats.item.premise.base.armor.BaseRiderArmor#unequip} 解除变身时触发
 */
public class RiderArmorRemoveEvent extends Event {
    private final LivingEntity entity;
    private final EquipmentSlot slot;

    public RiderArmorRemoveEvent(LivingEntity entity, EquipmentSlot slot) {
        this.entity = entity;
        this.slot = slot;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }


    /**
     * 脱装备之<b>前</b>,可取消
     * @see com.ankrya.doomsgreats.item.premise.base.armor.BaseRiderArmor#unequip
     */
    public static class Pre extends RiderArmorRemoveEvent {
        boolean isCanceled = false;

        public Pre(LivingEntity entity, EquipmentSlot slot) {
            super(entity, slot);
        }
        public boolean canRun() {
            return !isCanceled;
        }

        public void setCanceled(boolean cancel){
            this.isCanceled = cancel;
        }
    }

    /**
     * 脱装备之<b>后</b>
     * @see com.ankrya.doomsgreats.item.premise.base.armor.BaseRiderArmor#unequip
     */
    public static class Post extends RiderArmorRemoveEvent {
        public Post(LivingEntity entity, EquipmentSlot slot) {
            super(entity, slot);
        }
    }
}
