package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.init.ClassRegister;
import com.ankrya.doomsgreats.item.base.BaseRiderArmor;
import com.ankrya.doomsgreats.item.material.GreatsArmorMaterial;
import com.ankrya.doomsgreats.item.renderer.DoomGreatsArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import java.util.HashMap;
import java.util.Map;
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

    @Override
    public Map<Holder<MobEffect>, Integer> getEffects() {
        Map<Holder<MobEffect>, Integer> effects = new HashMap<>();
        effects.put(MobEffects.DAMAGE_RESISTANCE, 3);
        effects.put(MobEffects.MOVEMENT_SPEED, 7);
        effects.put(MobEffects.DIG_SPEED, 2);
        effects.put(MobEffects.JUMP, 3);
        effects.put(MobEffects.FIRE_RESISTANCE, 0);
        effects.put(MobEffects.WATER_BREATHING, 0);
        effects.put(MobEffects.NIGHT_VISION, 0);
        effects.put(MobEffects.DAMAGE_BOOST, 14);
        effects.put(MobEffects.CONDUIT_POWER, 0);
        effects.put(MobEffects.SATURATION, 9);
        return effects;
    }
}
