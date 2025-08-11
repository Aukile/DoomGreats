package com.ankrya.doomsgreats.api.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public class RiderArmorEquipEvent extends Event {
    private final LivingEntity entity;
    private final EquipmentSlot slot;
    private final ItemStack stack;
    boolean isCanceled = false;

    public RiderArmorEquipEvent(LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
        this.entity = entity;
        this.slot = slot;
        this.stack = stack;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean canRun() {
        return !isCanceled;
    }

    public void setCanceled(boolean cancel){
        this.isCanceled = cancel;
    }
}
