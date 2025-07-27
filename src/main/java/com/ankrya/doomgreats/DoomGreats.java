package com.ankrya.doomgreats;

import com.ankrya.doomgreats.init.ClassRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DoomGreats.MODID)
public class DoomGreats {
    public static final String MODID = "doom_greats";
    public static final Logger LOGGER = LogManager.getLogger(DoomGreats.class);
    public DoomGreats(IEventBus bus, ModContainer modContainer) {
        ClassRegister.init(bus);
    }
}
