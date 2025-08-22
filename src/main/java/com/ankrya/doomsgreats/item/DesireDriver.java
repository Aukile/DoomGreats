package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.init.ClassRegister;
import com.ankrya.doomsgreats.item.base.armor.BaseDriver;
import com.ankrya.doomsgreats.item.base.armor.BaseGeoArmor;
import com.ankrya.doomsgreats.item.material.GreatsArmorMaterial;
import com.ankrya.doomsgreats.item.renderer.DesireDriverRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animation.*;

import java.util.function.Consumer;

public class DesireDriver extends BaseDriver {
    public static final String BUCKLE = "buckle";
    public static final String REVOLVE = "revolve_on";
    public static final String IDLE0 = "first"; // 变身前闲置动画名
    public static final String IDLE = "idle"; // 通常闲置动画名

    public DesireDriver(Properties properties) {
        super(GreatsArmorMaterial.DGP_BLANK, Type.LEGGINGS, properties);
    }

    public static ItemStack getNewArmor(){
        return new ItemStack(ClassRegister.getRegisterObject("desire_driver", Item.class).get());
    }

    @Override
    public @NotNull Holder<SoundEvent> getEquipSound() {
        return new Holder.Direct<>(ClassRegister.getRegisterObject("desire_driver", SoundEvent.class).get());
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private DesireDriverRenderer renderer;

            @Override
            public <T extends LivingEntity> @NotNull HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (renderer == null) renderer = new DesireDriverRenderer();
                return renderer;
            }
        });
    }

    @Override
    public String getModel() {
        return "desire_driver";
    }

    @Override
    public String getTexture() {
        return "dooms_geats_belt";
    }

    @Override
    public Class<? extends BaseDriver> getDriverClass() {
        return DesireDriver.class;
    }
}
