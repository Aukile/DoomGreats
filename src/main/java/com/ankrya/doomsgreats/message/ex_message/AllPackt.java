package com.ankrya.doomsgreats.message.ex_message;

import com.ankrya.doomsgreats.interfaces.message.INMessage;
import com.ankrya.doomsgreats.message.NMessageCreater;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于发送网络包至服务器再发送至客户端（仅限于{@link INMessage}的实现类）
 */
public class AllPackt implements INMessage {
    List<Integer> types = new ArrayList<>();
    List<?> values = new ArrayList<>();
    final String clazz;
    final INMessage message;

    public AllPackt(String name, INMessage message, boolean hasData, int data) {
        this.clazz = name;
        this.message = message;
        if (this.message instanceof AllPackt)
            throw new RuntimeException("AllPackt can't be AllPackt");
    }

    public AllPackt(INMessage message){
        this(message.getClass().getName(), message, true, 2);
    }

    public AllPackt(String name, INMessage message){
        this(name, message, true, 2);
    }

    public AllPackt(Class<?> clazz, INMessage message) {
        this(clazz.getName(), message, true, 2);
    }

    @Override
    public void toBytes(@NotNull FriendlyByteBuf buf) {
        message.toBytes(buf);
        buf.writeUtf(clazz);
    }

    @Override
    public void run(IPayloadContext ctx) {
        ctx.enqueueWork(()->{
            if (!(message instanceof AllPackt)) {
                if (ctx.flow().isServerbound())
                    ctx.reply(new NMessageCreater(message));
                else message.run(ctx);
            }
        });
    }
}
