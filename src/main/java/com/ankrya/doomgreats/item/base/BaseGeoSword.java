package com.ankrya.doomgreats.item.base;

import com.ankrya.doomgreats.item.renderer.BaseGeoItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.SwordItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public abstract class BaseGeoSword extends SwordItem implements GeoItem {
    public String model;
    public String texture;
    public String animation;
    public RenderType renderType = null;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseGeoSword(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public BaseGeoSword(Properties properties, String model, String texture, String animation) {
        this(properties);
        this.model = model;
        this.texture = texture;
        this.animation = animation;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BaseGeoItemRenderer renderer;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (renderer == null) renderer = new BaseGeoItemRenderer();
                return renderer;
            }
        });
    }

    private PlayState predicate(AnimationState<BaseGeoSword> state) {
//        ItemStack stack = state.getData(DataTickets.ITEMSTACK);
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public String getAnimation() {
        return animation;
    }

    public String getModel() {
        return model;
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
