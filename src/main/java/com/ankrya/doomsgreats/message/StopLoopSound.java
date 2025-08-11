package com.ankrya.doomsgreats.message;

import com.ankrya.doomsgreats.interfaces.IEXMessage;
import com.ankrya.doomsgreats.interfaces.ISoundMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class StopLoopSound implements IEXMessage {
    private final int id;
    private final int type;
    private final ResourceLocation location;

    public StopLoopSound(int id, int type, ResourceLocation location) {
        this.id = id;
        this.type = type;
        this.location = location;
    }

    @Override
    public void toBytes(@NotNull FriendlyByteBuf buf) {
        IEXMessage.super.toBytes(buf);
        IEXMessage.writeInt(buf, id);
        IEXMessage.writeInt(buf, type);
        IEXMessage.writeResourceLocation(buf, location);
    }

    @Override
    public void run(IPayloadContext ctx) {
        try (Level level = ctx.player().level()) {
            Entity entity = level.getEntity(id);
            if (entity instanceof ISoundMap soundMap)
                soundMap.doomGreats$removeLoopSound(location);
            stopSound(Minecraft.getInstance(), this);
        } catch (Exception ignored) {
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void stopSound(Minecraft instance, StopLoopSound message) {
        instance.getSoundManager().stop(message.location, SoundSource.PLAYERS);
    }

    @Override
    public boolean hasData() {
        return true;
    }

    @Override
    public int dataLong() {
        return 3;
    }
}
