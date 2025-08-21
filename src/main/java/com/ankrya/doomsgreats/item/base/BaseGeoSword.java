package com.ankrya.doomsgreats.item.base;

import com.ankrya.doomsgreats.item.renderer.base.BaseGeoItemRenderer;
import com.ankrya.doomsgreats.item.renderer.base.BaseGeoSwordRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
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
    public String animation = "idle";
    public RenderType renderType = null;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseGeoSword(Tier tier, Properties properties) {
        super(tier, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.model = defaultModel();
        this.texture = defaultTexture();
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BaseGeoSwordRenderer renderer;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (renderer == null) renderer = new BaseGeoSwordRenderer();
                return renderer;
            }
        });
    }

    private PlayState predicate(AnimationState<BaseGeoSword> state) {
//        ItemStack stack = state.getData(DataTickets.ITEMSTACK);
        state.getController().setAnimation(RawAnimation.begin().then(getAnimation(), Animation.LoopType.PLAY_ONCE));
        if(state.getController().getAnimationState() == AnimationController.State.STOPPED)
            state.resetCurrentAnimation();
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

    public void setModel(String model) {
        this.model = model;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
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

    public abstract String defaultModel();

    public abstract String defaultTexture();
}
