package com.ankrya.doomsgreats.item.renderer;

import com.ankrya.doomsgreats.help.json.ItemHelp;
import com.ankrya.doomsgreats.item.DesireDriver;
import com.ankrya.doomsgreats.item.renderer.base.BaseRiderArmorRender;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib.cache.object.GeoBone;

public class DesireDriverRenderer extends BaseRiderArmorRender<DesireDriver> {
    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        boolean hasBuckle = ItemHelp.getNbt(this.currentStack).getBoolean("buckle");
        GeoBone buckle = this.model.getBone("Buckle").orElse(null);
        if (buckle != null) {
            buckle.setHidden(!hasBuckle);
        }
    }
}
