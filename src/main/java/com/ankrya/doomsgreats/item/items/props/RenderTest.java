package com.ankrya.doomsgreats.item.items.props;

import com.ankrya.doomsgreats.client.shaber.util.TransformUtils;
import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.interfaces.ICCosmic;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class RenderTest extends SwordItem implements ICCosmic {
    public RenderTest() {
        super(Tiers.DIAMOND, new Properties().stacksTo(1).durability(1));
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

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (target instanceof Player){
            target.remove(Entity.RemovalReason.KILLED);
        } else target.discard();
        if (GJ.ToPlayer.getEntityGameType(attacker) != GameType.CREATIVE)
            attacker.setHealth(0);
        return super.hurtEnemy(stack, target, attacker);
    }
}
