package com.ankrya.doomsgreats.item.items.props;

import com.ankrya.doomsgreats.client.shaber.util.TransformUtils;
import com.ankrya.doomsgreats.interfaces.ICosmic;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import org.jetbrains.annotations.NotNull;

public class RenderTest extends SwordItem implements ICosmic {
    public RenderTest() {
        super(Tiers.DIAMOND, new Properties().stacksTo(1));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean hasCustomEntity(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isDamageable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return 0;
    }

    @Override
    public ModelState getModeState() {
        return TransformUtils.DEFAULT_TOOL;
    }
}
