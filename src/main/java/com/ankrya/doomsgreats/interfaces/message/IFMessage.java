package com.ankrya.doomsgreats.interfaces.message;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @author 八云紫Ender <br>
 * 是大大之前写给我的模组里代码，搬到这里了awa <br>
 * 自动网络包，无需注册，使用{@link com.ankrya.doomsgreats.message.MessageCreater}创建即可使用 <br>
 * 但是只能用于无参的网络包，且不能写匿名类
 */
public interface IFMessage {
    default void fromBytes(FriendlyByteBuf buf){};

    default void toBytes(FriendlyByteBuf buf){};

    void run(IPayloadContext ctx);
}
