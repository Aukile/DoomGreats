package com.ankrya.doomsgreats.item.items.armor;

import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.init.ClassRegister;
import com.ankrya.doomsgreats.item.premise.base.armor.BaseDriver;
import com.ankrya.doomsgreats.item.premise.base.armor.BaseRiderArmor;
import com.ankrya.doomsgreats.item.premise.base.armor.BaseRiderArmorBase;
import com.ankrya.doomsgreats.item.premise.material.GreatsArmorMaterial;
import com.ankrya.doomsgreats.item.premise.renderer.DoomGreatsArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DoomsGreatsArmor extends BaseRiderArmor {
    /**披风物理模式*/
    public static final String CLOAK_EFFECT = "cloak_effect";

    /**变身时动画*/
    public static final String HENSHIN = "henshin";
    /**空闲时动画*/
    public static final String IDLE = "idle";
    public DoomsGreatsArmor(Properties properties, EquipmentSlot slot) {
        super(GreatsArmorMaterial.DOOMS_GREATS_ARMOR, properties, slot);
    }

    public static ItemStack getNewArmor(EquipmentSlot slot) {
        if (slot == EquipmentSlot.LEGS) return ItemStack.EMPTY;
        return new ItemStack(ClassRegister.getRegisterObject("dooms_greats_" + slot.getName(), Item.class).get());
    }

    public static void equipOrRemoveAll(LivingEntity entity){
        boolean equip = isAllEquip(entity);
        for (EquipmentSlot slot : BaseRiderArmorBase.getSlots()){
            if (equip) BaseRiderArmor.unequip(entity, slot);
            else BaseRiderArmor.equip(entity, slot, DoomsGreatsArmor.getNewArmor(slot));
        }
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

    public static void cloakEffect(LivingEntity entity, boolean active){
        cloakEffect(entity.getItemBySlot(EquipmentSlot.CHEST), active);
    }

    public static void cloakEffect(ItemStack stack, boolean active){
        GJ.ToItem.setNbt(stack, nbt -> nbt.putBoolean(CLOAK_EFFECT, active));
    }

    @Override
    public String getModel() {
        return "dooms_geats_armor";
    }

    @Override
    public String getTexture() {
        return "dooms_greats";
    }

    public static ItemAttributeModifiers addAttributes (EquipmentSlot slot){
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(Attributes.MAX_HEALTH, new AttributeModifier(GJ.Easy.getResource("dooms_geats_armor_health"), 40, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(slot));
        return builder.build();
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

    @Override
    public Class<? extends BaseDriver> getDriverClass() {
        return DesireDriver.class;
    }

    @Override
    public Class<? extends BaseRiderArmor> getArmorClass() {
        return DoomsGreatsArmor.class;
    }
}
