package com.ankrya.doomsgreats.item.items.weapon;

import com.ankrya.doomsgreats.client.sound.SoundName;
import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.item.premise.base.BaseGeoSword;
import com.ankrya.doomsgreats.item.premise.renderer.base.BaseGeoItemRenderer;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.SimpleTier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GoldenGeatsBusterQB9 extends BaseGeoSword {
    public static final Tier TIER = new SimpleTier(BlockTags.INCORRECT_FOR_GOLD_TOOL, 2000, 6f, 3.6f, 0, Ingredient::of);
    /**模式nbt*/
    public static final String QB9_MODE = "qb9_mode";
    /**充能nbt*/
    public static final String CHARGE = "charge";
    /**最大充能*/
    public static final String CHARGE_MAX = "charge_max";

    public String model = "dooms_geats_qb9";
    public GoldenGeatsBusterQB9() {
        super(TIER, new Item.Properties().stacksTo(1)
                .attributes(SwordItem.createAttributes(TIER, 8.4f, -2.4f))
                .component(DataComponents.UNBREAKABLE, new Unbreakable(true)));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    public static float getPowerForTime(int charge) {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeCharged) {
        livingEntity.setNoGravity(false);
        int time = livingEntity.getTicksUsingItem();
        if (time < 0) return;
        if (GJ.ToItem.getNbt(stack).getBoolean(CHARGE_MAX)){
            GJ.ToItem.setNbt(stack, nbt -> nbt.putBoolean(CHARGE, false));
            GJ.ToItem.setNbt(stack, nbt -> nbt.putBoolean(CHARGE_MAX, false));
        } else if (GJ.ToItem.getNbt(stack).getBoolean(CHARGE)){
            GJ.ToItem.setNbt(stack, nbt -> nbt.putBoolean(CHARGE, false));
        }
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
        livingEntity.setDeltaMovement(Vec3.ZERO);
        if (remainingUseDuration < 71996) GJ.ToItem.setNbt(stack, nbt -> nbt.putBoolean(CHARGE, true));
        if (remainingUseDuration < 71991) GJ.ToItem.setNbt(stack, nbt -> nbt.putBoolean(CHARGE_MAX, true));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> use = super.use(level, player, usedHand);
        ItemStack stack = use.getObject();
        if (player.isShiftKeyDown()){
            int oldMode = GJ.ToItem.getNbt(stack).getInt(QB9_MODE);
            GJ.ToItem.setNbt(stack, nbt -> nbt.putInt(QB9_MODE, oldMode == 0 ? 1 : 0));
            if (oldMode == 1) {
                this.setModel("dooms_geats_qb9");
                GJ.ToPlayer.playSound(player, SoundName.BLADE);
            }
            else {
                this.setModel("dooms_geats_qb9_gun");
                GJ.ToPlayer.playSound(player, SoundName.GUN);
            }
        } else if (stack.getItem() instanceof GoldenGeatsBusterQB9){
            player.startUsingItem(usedHand);
            player.setNoGravity(true);
            player.setDeltaMovement(Vec3.ZERO);
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
        return "qb_9_charge";
    }

    @Override
    public boolean autoGlow() {
        return true;
    }

    @Override
    public Map<String, Boolean> visibilityBones(BaseGeoItemRenderer<?> renderer) {
        ItemStack stack = renderer.getCurrentItemStack();
        return Map.of("BladeChange", !GJ.ToItem.getNbt(stack).getBoolean(CHARGE));
    }

    //渲染好难qwq
//    @Override
//    public GeoRenderLayer<?>[] getRenderLayers(GeoRenderer<?> renderer) {
//        List<ResourceLocation> textures = new ArrayList<>();
//        textures.add(GJ.Easy.getResource("item/qb_9_cosmic_gray"));
//        return new GeoRenderLayer[]{new CosmicGeoRenderLayer<>((GeoItemRenderer<?>) renderer, textures)};
//    }
}
