package com.ankrya.doomsgreats.item.material;

import com.ankrya.doomsgreats.DoomsGreats;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.EnumMap;
import java.util.List;

@EventBusSubscriber
public class GreatsArmorMaterial {
    private static final ResourceLocation DOOMS_GREATS_ARMOR_LOCATION = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "dooms_greats_armor");
    private static final ResourceLocation DGP_BLANK_LOCATION = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "dgp_blank");
    public static final SoundEvent DGP_BLANK_SOUND = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "desire_driver"));
    public static Holder<ArmorMaterial> DGP_BLANK = null;
    public static Holder<ArmorMaterial> DOOMS_GREATS_ARMOR = null;

    @SubscribeEvent
    public static void registerArmorMaterials(RegisterEvent event) {
        event.register(Registries.ARMOR_MATERIAL, registerHelper -> {
            ArmorMaterial doomsGreatsArmor = GreatsArmorMaterial.doomsGreatsArmor();
            ArmorMaterial dgpBlank = GreatsArmorMaterial.dgpBlank();
            registerHelper.register(DOOMS_GREATS_ARMOR_LOCATION, doomsGreatsArmor);
            registerHelper.register(DGP_BLANK_LOCATION, dgpBlank);
            DOOMS_GREATS_ARMOR = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(doomsGreatsArmor);
            DGP_BLANK = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(dgpBlank);
        });
        event.register(Registries.SOUND_EVENT, registerHelper -> {
            registerHelper.register(DGP_BLANK_LOCATION, DGP_BLANK_SOUND);
        });
    }
    public static net.minecraft.world.item.ArmorMaterial doomsGreatsArmor() {

        return createMaterial(DOOMS_GREATS_ARMOR_LOCATION, 15, 18 , 5 , 12, 10, 0, 8.6f, 20f, SoundEvents.EMPTY, Ingredient.of());
    }

    public static net.minecraft.world.item.ArmorMaterial dgpBlank() {

        return createMaterial(DGP_BLANK_LOCATION, 5, 5 , 5 , 5, 5, 0, 0f, 0f, SoundEvents.EMPTY, Ingredient.of());
    }

    public static net.minecraft.world.item.ArmorMaterial createMaterial(ResourceLocation resourceLocation, int helmet, int chestplate, int leggings, int boots, int body, int enchantmentValue, float toughness, float knockbackResistance, SoundEvent soundEvent, Ingredient repairIngredient) {
        return new net.minecraft.world.item.ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, boots);
            map.put(ArmorItem.Type.LEGGINGS, leggings);
            map.put(ArmorItem.Type.CHESTPLATE, chestplate);
            map.put(ArmorItem.Type.HELMET, helmet);
            map.put(ArmorItem.Type.BODY, body);
        }), enchantmentValue, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent)
                , () -> repairIngredient
                , List.of(new net.minecraft.world.item.ArmorMaterial.Layer(resourceLocation))
                , toughness, knockbackResistance);
    }
}
