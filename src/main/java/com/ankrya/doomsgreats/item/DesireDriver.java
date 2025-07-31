package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.item.base.BaseGeoArmor;

import static com.ankrya.doomsgreats.init.RegisterArmorMaterial.DGP_BLANK;

public class DesireDriver extends BaseGeoArmor {

    public DesireDriver(Properties properties) {
        super(DGP_BLANK, Type.LEGGINGS, properties);
        this.model = "desire_driver";
        this.texture = "desire_driver";
    }
}
