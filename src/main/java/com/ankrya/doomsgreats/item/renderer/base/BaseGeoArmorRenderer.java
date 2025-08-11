package com.ankrya.doomsgreats.item.renderer.base;

import com.ankrya.doomsgreats.item.base.armor.BaseGeoArmor;
import com.ankrya.doomsgreats.item.model.base.BaseGeoArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BaseGeoArmorRenderer<T extends BaseGeoArmor> extends GeoArmorRenderer<T> implements IClientItemExtensions{
    public BaseGeoArmorRenderer() {
        super(new BaseGeoArmorModel<>());
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable.getRenderType() == null) return super.getRenderType(animatable, texture, bufferSource, partialTick);
        return animatable.getRenderType();
    }

    @Override
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull EquipmentSlot equipmentSlot, @NotNull HumanoidModel<?> original) {
        return this;
    }
}
