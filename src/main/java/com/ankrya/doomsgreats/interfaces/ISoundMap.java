package com.ankrya.doomsgreats.interfaces;

import com.ankrya.doomsgreats.client.LoopSound;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface ISoundMap {
    Map<ResourceLocation, LoopSound> doomGreats$getLoopSounds();
    void doomGreats$addLoopSound(ResourceLocation id, LoopSound sound);
    void doomGreats$removeLoopSound(ResourceLocation id);
}
