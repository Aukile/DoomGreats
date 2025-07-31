package com.ankrya.doomgreats.item.renderer;

import com.ankrya.doomgreats.item.DoomGreatsArmor;
import com.ankrya.doomgreats.item.layer.DoomGreatsArmorLight;

public class DoomGreatsArmorRenderer extends BaseGeoArmorRenderer<DoomGreatsArmor>{
    public DoomGreatsArmorRenderer() {
        super();
        this.addRenderLayer(new DoomGreatsArmorLight(this));
    }

}
