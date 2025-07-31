package com.ankrya.doomsgreats;

import com.ankrya.doomsgreats.init.ClassRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DoomsGreats.MODID)
public class DoomsGreats {
    public static final String MODID = "dooms_greats";
    public static final Logger LOGGER = LogManager.getLogger(DoomsGreats.class);
    public DoomsGreats(IEventBus bus, ModContainer modContainer) {
//        ClassRegister.REGISTER.register(bus);
        ClassRegister.init(bus);
    }
}
