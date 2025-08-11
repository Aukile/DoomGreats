package com.ankrya.doomsgreats;

import com.ankrya.doomsgreats.data.ModVariable;
import com.ankrya.doomsgreats.data.Variables;
import com.ankrya.doomsgreats.init.ClassRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DoomsGreats.MODID)
public class DoomsGreats {
    public static final String MODID = "dooms_greats";
    public static final Logger LOGGER = LogManager.getLogger(DoomsGreats.class);
    public DoomsGreats(IEventBus bus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ClassRegister.init(bus);
        Variables.ATTACHMENT_TYPES.register(bus);
    }
}
