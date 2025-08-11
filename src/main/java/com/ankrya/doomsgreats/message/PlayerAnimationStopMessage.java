package com.ankrya.doomsgreats.message;

import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.interfaces.IEXMessage;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerAnimationStopMessage implements IEXMessage {
    final UUID uuid;
    final ResourceLocation layer;
    final int fadeTime;

    public PlayerAnimationStopMessage(UUID uuid, ResourceLocation layer, int fadeTime) {
        this.uuid = uuid;
        this.layer = layer;
        this.fadeTime = fadeTime;
    }

    @Override
    public void toBytes(@NotNull FriendlyByteBuf buf) {
        IEXMessage.super.toBytes(buf);
        IEXMessage.writeUUID(buf, uuid);
        IEXMessage.writeResourceLocation(buf, layer);
        IEXMessage.writeInt(buf, fadeTime);
    }

    @Override
    public void run(IPayloadContext ctx) {
        try (Level level = ctx.player().level()) {
            Player player = level.getPlayerByUUID(uuid);
            if (player instanceof AbstractClientPlayer clientPlayer){
                PlayerAnimator.stopAnimation(clientPlayer, layer, fadeTime);
            }
        } catch (Exception ignored) {
        }
    }

    public static void stopAnimation(AbstractClientPlayer player, ResourceLocation dataId, int fadeTime){
        PlayerAnimator.stopAnimation(player, dataId, fadeTime);
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
