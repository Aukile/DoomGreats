package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.item.base.BaseRiderArmor;
import com.ankrya.doomsgreats.item.renderer.DoomGreatsArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import java.util.function.Consumer;

import static com.ankrya.doomsgreats.init.RegisterArmorMaterial.DOOMS_GREATS_ARMOR;

public class DoomGreatsArmor extends BaseRiderArmor {
    public DoomGreatsArmor(Properties properties, EquipmentSlot slot) {
        super(DOOMS_GREATS_ARMOR, properties, slot);
        this.model = "dooms_greats";
        this.texture = "dooms_greats";
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
}
