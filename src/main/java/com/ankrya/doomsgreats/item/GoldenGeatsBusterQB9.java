package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.base.BaseGeoSword;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class GoldenGeatsBusterQB9 extends BaseGeoSword {
    public static final Tier TIER = new Tier() {
        @Override
        public int getUses() {
            return 0;
        }

        @Override
        public float getSpeed() {
            return 6f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3.6f;
        }

        @Override
        public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_GOLD_TOOL;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of();
        }
    };
    public static final String QB9_MODE = "qb9_mode";

    public GoldenGeatsBusterQB9() {
        super(TIER, new Item.Properties().stacksTo(1)
                .attributes(SwordItem.createAttributes(TIER, 8.4f, -2.4f)));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> use = super.use(level, player, usedHand);
        ItemStack stack = use.getObject();
        if (player.isShiftKeyDown()){
            int oldMode = ItemHelp.getNbt(stack).getInt(QB9_MODE);
            ItemHelp.setNbt(stack, nbt -> nbt.putInt(QB9_MODE, oldMode == 0 ? 1 : 0));
            if (oldMode == 1) {
                this.setModel("buster_qb_9_sword");
                HTool.playSound(player, "blade");
            }
            else {
                this.setModel("buster_qb_9_gun");
                HTool.playSound(player, "gun");
            }

        }
        return use;
    }

    @Override
    public String defaultModel() {
        return "dooms_geats_qb9";
    }

    @Override
    public String defaultTexture() {
        return "qb_9";
    }
}
