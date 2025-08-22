package com.ankrya.doomsgreats.item.renderer;

import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.DesireDriver;
import com.ankrya.doomsgreats.item.renderer.base.BaseRiderArmorRender;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;

public class DesireDriverRenderer extends BaseRiderArmorRender<DesireDriver> {
    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        boolean animation = ItemHelp.getNbt(this.currentStack).getBoolean(DesireDriver.REVOLVE);
        boolean hasBuckle = ItemHelp.getNbt(this.currentStack).getBoolean(DesireDriver.BUCKLE);
        this.model.getBone("Buckle").ifPresent(buckle -> buckle.setHidden(!hasBuckle));
        this.model.getBone("ysmGlowBoostFire").ifPresent(buckle -> buckle.setHidden(!animation));
    }
}