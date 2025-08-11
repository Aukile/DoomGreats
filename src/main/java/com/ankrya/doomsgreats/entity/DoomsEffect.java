package com.ankrya.doomsgreats.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DoomsEffect extends SpecialEffect{
    public DoomsEffect(EntityType<?> type, Level level) {
        super(type, level);
    }

    public DoomsEffect(Level level, Player owner) {
        super(level, owner, "dooms_geatshenxin", "dooms_greatshenxin", (int) (25.08 * 20));
        this.setAnimationName("henshin");
    }
}
