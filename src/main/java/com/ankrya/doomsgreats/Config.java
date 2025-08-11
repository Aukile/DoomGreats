package com.ankrya.doomsgreats;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    public static final ModConfigSpec.Builder BUILDER;
    public static ModConfigSpec.ConfigValue<Float> ATTACK_BASE;

    public static final ModConfigSpec SPEC;

    static {
        BUILDER = new ModConfigSpec.Builder();
        ATTACK_BASE = BUILDER.comment("Base attack damage for Dooms Greats").define("attackBase", 1.0F);

        SPEC = BUILDER.build();
    }

    public static float attackBase() {
        return ATTACK_BASE.get();
    }
}
