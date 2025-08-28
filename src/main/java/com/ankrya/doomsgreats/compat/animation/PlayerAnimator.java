package com.ankrya.doomsgreats.compat.animation;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.interfaces.message.INMessage;
import com.ankrya.doomsgreats.message.*;
import com.ankrya.doomsgreats.message.ex_message.PlayerAnimationMessage;
import com.ankrya.doomsgreats.message.ex_message.PlayerAnimationStopMessage;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * 玩家动画的东东<br>
 * {@link PlayerAnimator#playerAnimation} 是播放的方法 <br>
 * {@link PlayerAnimator#stopAnimation} 是停止的方法 <br>
 * {@link PlayerAnimator#isAnimationPlaying} 是判断动画是否在播放中 <br>
 * <p>
 * {@link PlayerAnimator#ANIMATION} 一般播放动画的层级 <br>
 * {@link PlayerAnimator#RIDE} 播放骑乘动画的层级
 */
public class PlayerAnimator {
    public static final ResourceLocation ANIMATION = GJ.Easy.getResource("animation");
    public static final ResourceLocation RIDE = GJ.Easy.getResource("ride");
    private static final String PLAYER_ANIMATION = "playeranimator";
    private static boolean INSTALLED = false;
    public static void init() {
        INSTALLED = ModList.get().isLoaded(PLAYER_ANIMATION);
        if (installed()){
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ANIMATION, 100, player -> new ModifierLayer<>());
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(RIDE, 99, player -> new ModifierLayer<>());
        }
    }
    public static void playerAnimation(Player player, String animation, boolean override){
        playerAnimation(player, animation, true, false, override);
    }

    public static void playerAnimation(Player player, String animation, boolean showRightArm, boolean showLeftArm, boolean override){
        playerAnimation(player, ANIMATION, animation, showRightArm, showLeftArm, override);
    }

    public static void playerRiderAnimation(Player player, String animation, boolean override){
        playerRiderAnimation(player, animation, true, true, override);
    }

    public static void playerRiderAnimation(Player player, String animation, boolean showRightArm, boolean showLeftArm, boolean override){
        playerAnimation(player, RIDE, animation, showRightArm, showLeftArm, override);
    }

    public static void stopAnimation(Player player){
        stopAnimation(player, ANIMATION, 8);
    }

    public static void stopRiderAnimation(Player player){
        stopAnimation(player, RIDE, 8);
    }

    public static boolean isAnimationPlaying(Player player){
        if (player instanceof AbstractClientPlayer clientPlayer){
            return isAnimationPlaying(clientPlayer, ANIMATION);
        }
        return false;
    }

    public static boolean isRiderAnimationPlaying(Player player){
        if (player instanceof AbstractClientPlayer clientPlayer){
            return isAnimationPlaying(clientPlayer, RIDE);
        }
        return false;
    }

    public static void playerAnimation(Player player, ResourceLocation dataId, String animation, boolean showRightArm, boolean showLeftArm, boolean override){
        INMessage animationMessage = new PlayerAnimationMessage(player.getUUID(), dataId, animation, showRightArm, showLeftArm, override);
        if (player.level() instanceof ServerLevel serverLevel)
            PacketDistributor.sendToPlayersInDimension(serverLevel, new NMessageCreater(animationMessage));
        else MessageLoader.sendToServer(new NMessageCreater(animationMessage));
    }

    @SuppressWarnings("unchecked")
    public static boolean isAnimationPlaying(AbstractClientPlayer player, ResourceLocation dataId){
        if (installed()){
            var associatedData = PlayerAnimationAccess.getPlayerAssociatedData(player);
            var modifierLayer = (ModifierLayer<IAnimation>) associatedData.get(dataId);
            if (modifierLayer == null) {
                return false;
            }
            IAnimation animation = modifierLayer.getAnimation();
            return animation != null && animation.isActive();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static void playAnimation(AbstractClientPlayer player, ResourceLocation dataId, IAnimation keyframeAnimation, boolean override){
        if (installed()){
            var associatedData = PlayerAnimationAccess.getPlayerAssociatedData(player);
            var modifierLayer = (ModifierLayer<IAnimation>) associatedData.get(dataId);
            if (modifierLayer == null) {
                return;
            }
            IAnimation animation = modifierLayer.getAnimation();
            if (animation == null || (override || !animation.isActive())) {
                AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(8, Ease.INOUTSINE);
                modifierLayer.replaceAnimationWithFade(fadeModifier, keyframeAnimation);
            }
        }
    }

    public static void stopAnimation(Player player, ResourceLocation dataId, int fadeTime){
        PlayerAnimationStopMessage stopMessage = new PlayerAnimationStopMessage(player.getUUID(), dataId, fadeTime);
        if (player.level() instanceof ServerLevel serverLevel)
            PacketDistributor.sendToPlayersInDimension(serverLevel, new NMessageCreater(stopMessage));
    }

    @SuppressWarnings("unchecked")
    public static void stopAnimation(AbstractClientPlayer player, ResourceLocation dataId, int fadeTime) {
        if (installed()){
            var associatedData = PlayerAnimationAccess.getPlayerAssociatedData(player);
            var modifierLayer = (ModifierLayer<IAnimation>) associatedData.get(dataId);
            if (modifierLayer != null && modifierLayer.isActive()) {
                AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(fadeTime, Ease.INOUTSINE);
                modifierLayer.replaceAnimationWithFade(fadeModifier, null);
            }
        }
    }

    public static boolean installed() {
        return INSTALLED;
    }
}
