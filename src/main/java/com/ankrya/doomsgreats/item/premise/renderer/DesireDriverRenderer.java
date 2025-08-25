package com.ankrya.doomsgreats.item.premise.renderer;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.item.items.armor.DesireDriver;
import com.ankrya.doomsgreats.item.premise.renderer.base.BaseRiderArmorRender;
import net.minecraft.world.entity.EquipmentSlot;

public class DesireDriverRenderer extends BaseRiderArmorRender<DesireDriver> {
    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        boolean animation = GJ.ToItem.getNbt(this.currentStack).getBoolean(DesireDriver.REVOLVE);
        boolean hasBuckle = GJ.ToItem.getNbt(this.currentStack).getBoolean(DesireDriver.BUCKLE);
        this.model.getBone("Buckle").ifPresent(buckle -> buckle.setHidden(!hasBuckle));
        this.model.getBone("ysmGlowBoostFire").ifPresent(buckle -> buckle.setHidden(!animation));
    }
}