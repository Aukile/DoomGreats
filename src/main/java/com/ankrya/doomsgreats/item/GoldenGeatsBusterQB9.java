package com.ankrya.doomsgreats.item;

import com.ankrya.doomsgreats.client.sound.SoundName;
import com.ankrya.doomsgreats.help.HTool;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.item.base.BaseGeoSword;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.SimpleTier;
import org.jetbrains.annotations.NotNull;

public class GoldenGeatsBusterQB9 extends BaseGeoSword {
    public static final Tier TIER = new SimpleTier(BlockTags.INCORRECT_FOR_GOLD_TOOL, 2000, 6f, 3.6f, 0, Ingredient::of);
    public static final String QB9_MODE = "qb9_mode";

    public String model = "dooms_geats_qb9";
    public GoldenGeatsBusterQB9() {
        super(TIER, new Item.Properties().stacksTo(1)
                .attributes(SwordItem.createAttributes(TIER, 8.4f, -2.4f)));
    }

    @Override
    public boolean isDamageable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> use = super.use(level, player, usedHand);
        ItemStack stack = use.getObject();
        if (player.isShiftKeyDown()){
            int oldMode = ItemHelp.getNbt(stack).getInt(QB9_MODE);
            ItemHelp.setNbt(stack, nbt -> nbt.putInt(QB9_MODE, oldMode == 0 ? 1 : 0));
            if (oldMode == 1) {
                this.setModel("dooms_geats_qb9");
                HTool.playSound(player, SoundName.BLADE);
            }
            else {
                this.setModel("dooms_geats_qb9_gun");
                HTool.playSound(player, SoundName.GUN);
            }

        }
        return use;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            int count = 0;
            ItemStack firstStack = ItemStack.EMPTY;

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack inventoryStack = player.getInventory().getItem(i);
                if (inventoryStack.getItem() == this) {
                    count++;
                    if (firstStack.isEmpty()) {
                        firstStack = inventoryStack;
                    }
                    if (count > 1 && inventoryStack != firstStack) {
                        player.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return super.onEntitySwing(stack, entity, hand);
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getTexture() {
        return "qb_9";
    }
}
