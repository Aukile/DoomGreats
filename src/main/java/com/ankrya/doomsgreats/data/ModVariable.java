package com.ankrya.doomsgreats.data;

import net.minecraft.world.phys.Vec3;

public class ModVariable {
    /**Vec3变量，同步刀光位置*/
    public static final String WEAPON_POSITIONS = "weapon_positions";

    /**
     * 添加变量 <br>
     * @see Variables#registerVariable
     */
    public static void init(Variables variables){
        variables.registerVariable(WEAPON_POSITIONS, Vec3.ZERO, true);
    }
}
