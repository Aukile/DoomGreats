package com.ankrya.doomsgreats.message.ex_message;

import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.interfaces.message.INMessage;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerAnimationStopMessage implements INMessage {
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
        INMessage.autoWriteAll(buf, uuid, layer, fadeTime);
    }

    @Override
    public void run(IPayloadContext ctx) {
        Level level = ctx.player().level();
        Player player = level.getPlayerByUUID(uuid);
        if (player instanceof AbstractClientPlayer clientPlayer) {
            PlayerAnimator.stopAnimation(clientPlayer, layer, fadeTime);
        }
    }

    public static void stopAnimation(AbstractClientPlayer player, ResourceLocation dataId, int fadeTime){
        PlayerAnimator.stopAnimation(player, dataId, fadeTime);
    }
}
