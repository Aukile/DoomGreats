package com.ankrya.doomgreats.item;

import com.ankrya.doomgreats.item.base.BaseGeoArmor;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

public class DesireDriver extends BaseGeoArmor {
    public DesireDriver(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.LEGGINGS, properties);
        this.model = "desire_driver";
        this.texture = "desire_driver";
    }
}
