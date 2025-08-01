package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.init.ClassRegister;
import com.ankrya.doomsgreats.item.base.BaseRiderArmor;
import com.ankrya.doomsgreats.item.material.GreatsArmorMaterial;
import com.ankrya.doomsgreats.item.renderer.DoomGreatsArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import java.util.function.Consumer;

public class DoomGreatsArmor extends BaseRiderArmor {
    public DoomGreatsArmor(Properties properties, EquipmentSlot slot) {
        super(GreatsArmorMaterial.DOOMS_GREATS_ARMOR, properties, slot);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private DoomGreatsArmorRenderer renderer;

            @Override
            public <T extends LivingEntity> @NotNull HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (renderer == null) renderer = new DoomGreatsArmorRenderer();
                return renderer;
            }
        });
    }

    @Override
    public String defaultModel() {
        return "dooms_geats_armor";
    }

    @Override
    public String defaultTexture() {
        return "dooms_greats";
    }


    public static ItemStack getNewArmor(EquipmentSlot slot) {
        if (slot == EquipmentSlot.LEGS) return ItemStack.EMPTY;
        return new ItemStack(ClassRegister.getRegisterObject("dooms_greats_" + slot.getName(), Item.class).get());
    }
}
