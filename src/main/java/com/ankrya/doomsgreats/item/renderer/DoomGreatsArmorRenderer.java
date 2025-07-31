package com.ankrya.doomsgreats.item.renderer;

import com.ankrya.doomsgreats.item.DoomGreatsArmor;
import com.ankrya.doomsgreats.item.layer.DoomGreatsArmorLight;

public class DoomGreatsArmorRenderer extends BaseGeoArmorRenderer<DoomGreatsArmor>{
    public DoomGreatsArmorRenderer() {
        super();
        this.addRenderLayer(new DoomGreatsArmorLight(this));
    }

}
