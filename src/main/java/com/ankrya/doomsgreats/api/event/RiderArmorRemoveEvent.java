package com.ankrya.doomsgreats.api.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public class RiderArmorRemoveEvent extends Event {
    private final LivingEntity entity;
    private final EquipmentSlot slot;
    boolean isCanceled = false;

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

    public boolean canRun() {
        return !isCanceled;
    }

    public void setCanceled(boolean cancel){
        this.isCanceled = cancel;
    }
}
