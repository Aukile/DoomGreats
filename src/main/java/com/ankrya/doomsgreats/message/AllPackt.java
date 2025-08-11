package com.ankrya.doomsgreats.message;

import com.ankrya.doomsgreats.interfaces.IEXMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class AllPackt implements IEXMessage {
    final String clazz;
    final IEXMessage message;

    public AllPackt(String name, IEXMessage message, boolean hasData, int data) {
        this.clazz = name;
        this.message = message;
    }

    public AllPackt(IEXMessage message){
        this(message.getClass().getName(), message, true, 2);
    }

    public AllPackt(String name, IEXMessage message){
        this(name, message, true, 2);
    }

    public AllPackt(Class<?> clazz, IEXMessage message) {
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
