package com.ankrya.doomgreats.interfaces;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IFMessage {
    void fromBytes(FriendlyByteBuf buf);

    void toBytes(FriendlyByteBuf buf);

    void run(IPayloadContext ctx);
}
