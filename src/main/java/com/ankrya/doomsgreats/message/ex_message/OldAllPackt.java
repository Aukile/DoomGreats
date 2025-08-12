package com.ankrya.doomsgreats.message.ex_message;

import com.ankrya.doomsgreats.interfaces.IEXMessage;
import com.ankrya.doomsgreats.message.EXMessageCreater;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * {@link IEXMessage}的CTSTC网络包
 * 所以叫OldAllPackt
 */
public class OldAllPackt implements IEXMessage {
    final String clazz;
    final IEXMessage message;

    public OldAllPackt(String name, IEXMessage message, boolean hasData, int data) {
        this.clazz = name;
        this.message = message;
    }

    public OldAllPackt(IEXMessage message){
        this(message.getClass().getName(), message, true, 2);
    }

    public OldAllPackt(String name, IEXMessage message){
        this(name, message, true, 2);
    }

    public OldAllPackt(Class<?> clazz, IEXMessage message) {
        this(clazz.getName(), message, true, 2);
    }

    @Override
    public void toBytes(@NotNull FriendlyByteBuf buf) {
        message.toBytes(buf);
        IEXMessage.writeString(buf, clazz);
    }

    @Override
    public void run(IPayloadContext ctx) {
        try (Level level = ctx.player().level()) {
            PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new EXMessageCreater(message));
        } catch (Exception ignored) {}
    }

    @Override
    public boolean hasData() {
        return true;
    }

    @Override
    public int dataLong() {
        return message.dataLong() + 1;
    }
}
