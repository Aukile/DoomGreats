package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.DoomsGreats;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.EnumMap;
import java.util.List;

@EventBusSubscriber
public class RegisterArmorMaterial {
    public static Holder<ArmorMaterial> DOOMS_GREATS_ARMOR = null;
    public static Holder<ArmorMaterial> DGP_BLANK = null;

    @SubscribeEvent
    public static void registerArmorMaterial(RegisterEvent event) {
        ResourceLocation doomsGreatsArmor = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "dooms_greats_armor");
        event.register(Registries.ARMOR_MATERIAL, registerHelper -> {
            ArmorMaterial armorMaterial = new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 12);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 18);
                map.put(ArmorItem.Type.HELMET, 15);
                map.put(ArmorItem.Type.BODY, 10);
            }), 0, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY)
                    , () -> Ingredient.of(new ItemStack(Blocks.CHERRY_LOG))
                    , List.of(new ArmorMaterial.Layer(doomsGreatsArmor))
                    , 8f, 2f);
            registerHelper.register(doomsGreatsArmor, armorMaterial);
            DOOMS_GREATS_ARMOR = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(armorMaterial);
        });

        ResourceLocation dgpBlank = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "dgp_blank");
        event.register(Registries.ARMOR_MATERIAL, registerHelper -> {
            ArmorMaterial armorMaterial = new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 5);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 5);
                map.put(ArmorItem.Type.HELMET, 5);
                map.put(ArmorItem.Type.BODY, 5);
            }), 0, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY)
                    , () -> Ingredient.of(new ItemStack(Blocks.CHERRY_LOG))
                    , List.of(new ArmorMaterial.Layer(dgpBlank))
                    , 0f, 0f);
            registerHelper.register(dgpBlank, armorMaterial);
            DGP_BLANK = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(armorMaterial);
        });
    }
}
