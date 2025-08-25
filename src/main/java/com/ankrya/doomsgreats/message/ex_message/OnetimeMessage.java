package com.ankrya.doomsgreats.message.ex_message;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.interfaces.message.INMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OnetimeMessage implements INMessage {
    final String name;
    GJ onetime;

    public OnetimeMessage(GJ onetime) {
        this.name = onetime.getClass().getName();
    }

    public OnetimeMessage(String name) {
        this.name = name;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        INMessage.autoWriteAll(buf, name);
    }

    @Override
    public void run(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            GJ onetime = getOnetime(name, ctx.player());
            onetime.use();
        });
    }

    private static GJ getOnetime(String name, Player player) {
        try {
            return (GJ) Class.forName(name).getDeclaredConstructor(Player.class).newInstance(player);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
