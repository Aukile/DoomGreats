package com.ankrya.doomsgreats.interfaces.mod_use;

import com.ankrya.doomsgreats.client.sound.LoopSound;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface ISoundMap {
    Map<ResourceLocation, LoopSound> doomGreats$getLoopSounds();
    boolean doomGreats$containsLoopSound(ResourceLocation id);
    void doomGreats$addLoopSound(ResourceLocation id, LoopSound sound);
    void doomGreats$removeLoopSound(ResourceLocation id);
}
