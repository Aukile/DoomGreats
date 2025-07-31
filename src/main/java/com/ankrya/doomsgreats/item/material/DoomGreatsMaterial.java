package com.ankrya.doomsgreats.item.material;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum DoomGreatsMaterial implements Holder<ArmorMaterial> {
    DOOM_GREATS;

    private static final Map<ArmorItem.Type, Integer> DOOM_DEFENSE_VALUES = Map.of(
            ArmorItem.Type.BOOTS, 18,
            ArmorItem.Type.LEGGINGS, 5,
            ArmorItem.Type.CHESTPLATE, 20,
            ArmorItem.Type.HELMET, 16
    );

    @Override
    public ArmorMaterial value() {
        return new ArmorMaterial(
                DOOM_DEFENSE_VALUES,
                0,
                null,
                null,
                null,
                20,
                5
        );
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public boolean is(ResourceLocation resourceLocation) {
        return false;
    }

    @Override
    public boolean is(ResourceKey<ArmorMaterial> resourceKey) {
        return false;
    }

    @Override
    public boolean is(Predicate<ResourceKey<ArmorMaterial>> predicate) {
        return false;
    }

    @Override
    public boolean is(TagKey<ArmorMaterial> tagKey) {
        return false;
    }

    @Override
    public boolean is(Holder<ArmorMaterial> holder) {
        return false;
    }

    @Override
    public Stream<TagKey<ArmorMaterial>> tags() {
        return Stream.empty();
    }

    @Override
    public Either<ResourceKey<ArmorMaterial>, ArmorMaterial> unwrap() {
        return null;
    }

    @Override
    public Optional<ResourceKey<ArmorMaterial>> unwrapKey() {
        return Optional.empty();
    }

    @Override
    public Kind kind() {
        return null;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<ArmorMaterial> holderOwner) {
        return false;
    }
}
