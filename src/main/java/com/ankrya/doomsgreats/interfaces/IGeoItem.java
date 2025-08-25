package com.ankrya.doomsgreats.interfaces;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.item.premise.renderer.base.BaseGeoArmorRenderer;
import com.ankrya.doomsgreats.item.premise.renderer.base.BaseGeoItemRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;

import java.util.function.Consumer;

public interface IGeoItem extends GeoItem {
    /**nbt更改动画使用*/
    String ANIMATION = "run_animation";
    /**nbt重置动画使用，使用{@link IGeoItem#playAnimationAndReset}即可*/
    String ANIMATION_STOP = "animation_stop";

    @Override
    default void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            final boolean isArmor = IGeoItem.this instanceof ArmorItem;
            private BaseGeoItemRenderer<?> itemRenderer;
            private BaseGeoArmorRenderer<?> armorRenderer;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (!isArmor && itemRenderer == null) itemRenderer = new BaseGeoItemRenderer<>();
                return itemRenderer;
            }

            @Override
            public <T extends LivingEntity> @NotNull HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (isArmor && armorRenderer == null) armorRenderer = new BaseGeoArmorRenderer<>();
                return armorRenderer;
            }
        });
    }

    static void playAnimation(ItemStack itemStack, String animation){
        GJ.ToItem.setNbt(itemStack, nbt -> nbt.putString(ANIMATION, animation));
    }

    /**
     * 预备解决方案
     */
    static void playAnimationAndReset(ItemStack itemStack, String animation){
        GJ.ToItem.setNbt(itemStack, nbt -> nbt.putBoolean(ANIMATION_STOP, true));
        playAnimation(itemStack, animation);
    }

    private PlayState predicate(AnimationState<IGeoItem> state) {
        AnimationController<IGeoItem> controller = state.getController();
        ItemStack itemStack = state.getData(DataTickets.ITEMSTACK);

        if (itemStack != null && GJ.ToItem.getNbt(itemStack).getBoolean(ANIMATION_STOP)) {
            GJ.ToItem.setNbt(itemStack, nbt -> nbt.putBoolean(ANIMATION_STOP, false));
            controller.stop();
        }

        controller.setAnimation(RawAnimation.begin().then(getAnimation(itemStack), Animation.LoopType.PLAY_ONCE));
        if(controller.getAnimationState() == AnimationController.State.STOPPED)
            state.resetCurrentAnimation();
        return PlayState.CONTINUE;
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    default String getAnimation(ItemStack stack) {
        String animation = GJ.ToItem.getNbt(stack).getString(ANIMATION);
        return animation.isEmpty() ? getAnimation() : animation;
    }

    default String getAnimation() {
        return "idle";
    }

    default RenderType getRenderType() {
        return null;
    }

    String getModel();

    String getTexture();
}
