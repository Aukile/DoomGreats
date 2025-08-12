package com.ankrya.doomsgreats.message;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.message.common.AllPlayAnimation;
import com.ankrya.doomsgreats.message.common.PlayAnimation;
import com.ankrya.doomsgreats.message.common.SyncVariableMessage;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public final class MessageLoader {

    @SubscribeEvent
    public static void load(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(DoomsGreats.MODID);
        registrar.playBidirectional(SyncVariableMessage.TYPE, SyncVariableMessage.CODEC, new DirectionalPayloadHandler<>(SyncVariableMessage::handle, null));
        registrar.playBidirectional(MessageCreater.TYPE, MessageCreater.CODEC, new DirectionalPayloadHandler<>(MessageCreater::run,MessageCreater::run));
        registrar.playBidirectional(EXMessageCreater.TYPE, EXMessageCreater.CODEC, new DirectionalPayloadHandler<>(EXMessageCreater::run, EXMessageCreater::run));
        registrar.playBidirectional(NMessageCreater.TYPE, NMessageCreater.CODEC, new DirectionalPayloadHandler<>(NMessageCreater::run, NMessageCreater::run));
        registrar.playBidirectional(PlayAnimation.TYPE, PlayAnimation.CODEC, new DirectionalPayloadHandler<>(PlayAnimation::run,PlayAnimation::run));
        registrar.playBidirectional(AllPlayAnimation.TYPE, AllPlayAnimation.CODEC, new DirectionalPayloadHandler<>(AllPlayAnimation::run,AllPlayAnimation::run));
    }

    //下面的方法有点意义不明？ 额，当搬版本的集中处理器。。。大概

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayer(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayersNearby(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayersNear((ServerLevel) player.level(), player, player.getX(), player.getY(), player.getZ(), 64, message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayersInDimension(MSG message, Entity entity) {
        if (!entity.level().isClientSide)
            PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), message);
    }

    public static <MSG extends CustomPacketPayload> void sendToEntityAndSelf(MSG message, Entity entity) {
        if (!entity.level().isClientSide)
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
    }
}
