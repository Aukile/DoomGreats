package com.ankrya.doomgreats.message;

import com.ankrya.doomgreats.DoomGreats;
import com.ankrya.doomgreats.interfaces.IFMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class MessageCreater implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageCreater> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, "message_creater"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageCreater> CODEC = StreamCodec.of(MessageCreater::toBuf, MessageCreater::fromBuf);
    final IFMessage message;

    public MessageCreater(IFMessage message) {
        this.message = message;
    }

    public static void toBuf(FriendlyByteBuf buf, MessageCreater creater) {
        String path = creater.message.getClass().getName();
        byte[] bytes = path.getBytes();
        buf.writeInt(bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            buf.writeByte(b);
        }
        creater.message.toBytes(buf);
//		Main.LOGGER.info("create Buf by " + creater.message);
    }

    public static MessageCreater fromBuf(FriendlyByteBuf buf) {
        byte[] bytes = new byte[buf.readInt()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = buf.readByte();
        }
        String path = new String(bytes);
        IFMessage message;
        try {
            message = (IFMessage) Class.forName(path).getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        message.fromBytes(buf);
//		Main.LOGGER.info("read Buf in " + message);
        return new MessageCreater(message);
    }

    // 执行message对象的方法
    public static void run(final MessageCreater creater, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
//			Main.LOGGER.info("run task " + creater.message);
            creater.message.run(ctx);
        });
    }

    @Override
    public @NotNull Type<MessageCreater> type() {
        return TYPE;
    }
}