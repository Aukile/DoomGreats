package com.ankrya.doomsgreats.item.premise.base.armor;

import com.ankrya.doomsgreats.interfaces.IGeoItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseGeoArmor extends ArmorItem implements IGeoItem {
    public static final String ANIMATION = "run_animation"; //nbt更改动画使用
    public static final String ANIMATION_STOP = "animation_stop";

    public String animation = "idle";
    public RenderType renderType = null;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseGeoArmor(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    @Override
    public RenderType getRenderType() {
        return renderType;
    }

    @Override
    public String getAnimation() {
        return animation;
    }
}
