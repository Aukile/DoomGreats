package com.ankrya.doomsgreats.entity;

import com.ankrya.doomsgreats.init.ClassRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DoomsEffect extends SpecialEffectEntity {
    public DoomsEffect(EntityType<?> type, Level level) {
        this(type, level,null);
    }

    public DoomsEffect(EntityType<?> type, Level level, Player owner) {
        super(type, level, owner, "dooms_geatshenxin", "dooms_greatshenxin", (int) (11.72 * 20));
        this.setAnimationName("henshin");
        this.setAutoClear(true);
    }

    public DoomsEffect(Level level, Player owner){
        this(ClassRegister.getRegisterObject("dooms_effect", EntityType.class).get(), level, owner);
    }
}
