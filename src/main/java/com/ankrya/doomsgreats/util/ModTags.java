package com.ankrya.doomsgreats.util;

import com.ankrya.doomsgreats.help.GJ;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {

        public TagKey<Item> creatTag(String name) {
            return TagKey.create(Registries.ITEM, GJ.Easy.getResource(name));
        }
    }
    public static class Blocks {

        public TagKey<Block> creatTag(String name) {
            return TagKey.create(Registries.BLOCK, GJ.Easy.getResource(name));
        }
    }
}
