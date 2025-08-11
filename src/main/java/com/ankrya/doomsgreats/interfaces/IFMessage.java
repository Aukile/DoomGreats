package com.ankrya.doomsgreats.interfaces;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IFMessage {
    default void fromBytes(FriendlyByteBuf buf){};

    default void toBytes(FriendlyByteBuf buf){};

    void run(IPayloadContext ctx);
}
