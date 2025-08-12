package com.ankrya.doomsgreats.message.ex_message;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.interfaces.INMessage;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PlayerAnimationMessage implements INMessage {
    final UUID uuid;
    final ResourceLocation layer;
    final String animation;
    final boolean showRightArm;
    final boolean showLeftArm;
    final boolean override;

    public PlayerAnimationMessage(UUID uuid, ResourceLocation layer, String animation, boolean showRightArm, boolean showLeftArm, boolean override) {
        this.uuid = uuid;
        this.layer = layer;
        this.animation = animation;
        this.showRightArm = showRightArm;
        this.showLeftArm = showLeftArm;
        this.override = override;
    }

    @Override
    public void toBytes(@NotNull FriendlyByteBuf buf) {
        INMessage.autoWriteAll(buf, uuid, layer, animation, showRightArm, showLeftArm, override);
    }

    @Override
    public void run(IPayloadContext ctx) {
        Level level = ctx.player().level();
        Player player = level.getPlayerByUUID(uuid);
        if (player instanceof AbstractClientPlayer clientPlayer) {
            playerAnimation(clientPlayer, layer, animation, showRightArm, showLeftArm, override);
        }
    }

    public static void playerAnimation(AbstractClientPlayer player, ResourceLocation dataId, String animation, boolean showRightArm, boolean showLeftArm, boolean override){
        PlayerAnimator.playAnimation(player, dataId, Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, animation))).playAnimation()
                .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(showRightArm).setShowLeftItem(showLeftArm)), override);
    }
}
