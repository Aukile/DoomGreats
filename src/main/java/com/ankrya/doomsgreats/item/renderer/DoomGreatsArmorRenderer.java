package com.ankrya.doomsgreats.item.renderer;

import com.ankrya.doomsgreats.item.DoomGreatsArmor;
import com.ankrya.doomsgreats.item.layer.DoomGreatsArmorLight;
import com.ankrya.doomsgreats.item.renderer.base.BaseRiderArmorRender;

public class DoomGreatsArmorRenderer extends BaseRiderArmorRender<DoomGreatsArmor> {
    public DoomGreatsArmorRenderer() {
        super();
        this.addRenderLayer(new DoomGreatsArmorLight(this));
    }

}
