package com.ankrya.doomsgreats.message.ex_message;

import com.ankrya.doomsgreats.interfaces.message.IEXMessage;
import com.ankrya.doomsgreats.message.EXMessageCreater;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * {@link IEXMessage}的CTSTC网络包<br>
 * 应该不会用到<br>
 * 留例
 */
public class AllPacktForIEX implements IEXMessage {
    final String clazz;
    final IEXMessage message;

    public AllPacktForIEX(String name, IEXMessage message, boolean hasData, int data) {
        this.clazz = name;
        this.message = message;
    }

    public AllPacktForIEX(IEXMessage message){
        this(message.getClass().getName(), message, true, 2);
    }

    public AllPacktForIEX(String name, IEXMessage message){
        this(name, message, true, 2);
    }

    public AllPacktForIEX(Class<?> clazz, IEXMessage message) {
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
