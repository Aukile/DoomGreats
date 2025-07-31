package com.ankrya.doomsgreats.message;

import com.ankrya.doomsgreats.DoomsGreats;
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
        registrar.playBidirectional(MessageCreater.TYPE, MessageCreater.CODEC, new DirectionalPayloadHandler<>(MessageCreater::run,MessageCreater::run));
    }

    public <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    public <MSG extends CustomPacketPayload> void sendToPlayer(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public <MSG extends CustomPacketPayload> void sendToPlayersNearby(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayersNear((ServerLevel) player.level(), player, player.getX(), player.getY(), player.getZ(), 64, message);
    }

    public <MSG extends CustomPacketPayload> void sendToEntityAndSelf(MSG message, Entity entity) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
    }
}
