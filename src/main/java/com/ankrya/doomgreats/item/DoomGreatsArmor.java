package com.ankrya.doomgreats.item;

import com.ankrya.doomgreats.item.base.BaseRiderArmor;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

public class DoomGreatsArmor extends BaseRiderArmor {
    public DoomGreatsArmor(Holder<ArmorMaterial> material, Properties properties, EquipmentSlot slot) {
        super(material, properties, slot);
        this.model = "doom_greats";
        this.texture = "doom_greats";
    }
}
