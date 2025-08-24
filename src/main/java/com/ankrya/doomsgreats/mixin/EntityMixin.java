package com.ankrya.doomsgreats.mixin;

import com.ankrya.doomsgreats.client.sound.LoopSound;
import com.ankrya.doomsgreats.interfaces.mod_use.ISoundMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public class EntityMixin implements ISoundMap {
    @Unique
    public Map<ResourceLocation, LoopSound> doomGreats$loopSounds = new HashMap<>();

    @Override
    public Map<ResourceLocation, LoopSound> doomGreats$getLoopSounds() {
        return doomGreats$loopSounds;
    }

    @Override
    public boolean doomGreats$containsLoopSound(ResourceLocation id) {
        return doomGreats$loopSounds.containsKey(id);
    }

    @Override
    public void doomGreats$addLoopSound(ResourceLocation id, LoopSound sound) {
        doomGreats$loopSounds.put(id, sound);
    }

    @Override
    public void doomGreats$removeLoopSound(ResourceLocation id) {
        if (doomGreats$loopSounds.containsKey(id)){
            doomGreats$loopSounds.get(id).stopSound();
            doomGreats$loopSounds.remove(id);
        }
    }
}
