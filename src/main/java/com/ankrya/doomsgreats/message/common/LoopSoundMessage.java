package com.ankrya.doomsgreats.message.common;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.client.LoopSound;
import com.ankrya.doomsgreats.message.ex_message.PlayLoopSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.ankrya.doomsgreats.message.ex_message.PlayLoopSound.getSoundSource;

public class LoopSoundMessage implements CustomPacketPayload {
    public static final Type<LoopSoundMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "loop_sound_message"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LoopSoundMessage> CODEC = StreamCodec.of(LoopSoundMessage::toBuf, LoopSoundMessage::fromBuf);

    final ResourceLocation sound;
    final boolean loop;
    final int range;
    final int type;
    final int id;

    public LoopSoundMessage(ResourceLocation sound, boolean loop, int range, int type, int id) {
        this.sound = sound;
        this.loop = loop;
        this.range = range;
        this.type = type;
        this.id = id;
    }
    private static void toBuf(RegistryFriendlyByteBuf buf, LoopSoundMessage loopSoundMessage) {
        buf.writeResourceLocation(loopSoundMessage.sound);
        buf.writeBoolean(loopSoundMessage.loop);
        buf.writeInt(loopSoundMessage.range);
        buf.writeInt(loopSoundMessage.type);
        buf.writeInt(loopSoundMessage.id);
    }

    private static LoopSoundMessage fromBuf(RegistryFriendlyByteBuf buf) {
        final ResourceLocation sound = buf.readResourceLocation();
        final boolean loop = buf.readBoolean();
        final int range = buf.readInt();
        final int type = buf.readInt();
        final int id = buf.readInt();
        return new LoopSoundMessage(sound, loop, range, type, id);
    }

    @Override
    public @NotNull Type<LoopSoundMessage> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            PlayLoopSound.playLoopSound(Minecraft.getInstance(), new LoopSound(ctx.player().level().getEntity(id), sound, getSoundSource(type), loop, range));
        });
    }
}
