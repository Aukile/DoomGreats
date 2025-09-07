package com.ankrya.doomsgreats.mixin;

import com.ankrya.doomsgreats.client.particle.ParticleUtil;
import com.ankrya.doomsgreats.client.particle.base.advanced.AdvancedParticleData;
import com.ankrya.doomsgreats.client.particle.base.advanced.ParticleComponent;
import com.ankrya.doomsgreats.client.particle.base.advanced.RibbonParticleData;
import com.ankrya.doomsgreats.event.PlayerEvent;
import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.help.other.SlashLight;
import com.ankrya.doomsgreats.item.items.armor.DoomsGreatsArmor;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin implements IItemExtension {

    @Unique
    @Override
    public boolean onEntitySwing(ItemStack stack, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (stack.getItem() instanceof SwordItem && entity instanceof Player player && DoomsGreatsArmor.isAllEquip(entity)){
            ItemStack driver = GJ.ToItem.getDriver(entity);
            int time = GJ.ToItem.getNbt(driver).getInt(PlayerEvent.GREATS_HIT_SEGMENT);
            PlayerEvent.hit(driver, player, entity.level(), time);
            if (time == 3){
                return false;
            }
            else if (player instanceof AbstractClientPlayer clientPlayer){
                Vector3f slash = SlashLight.getQB9Position(clientPlayer)[0];
                Vec3 movement = clientPlayer.position();
                slash = slash.add((float) movement.x, (float) movement.y, (float) movement.z);
                GJ.AdvancedParticleHelper.addRobbin(entity.level(), AdvancedParticleData.getParticleType(),
                        slash.x(), slash.y(), slash.z(), 0.05, 0.05, 0.05, false
                        , 1, 0, 0, 0, 1, 255, 255, 255, 1, 1, 45, true, false,
                        GJ.AdvancedParticleHelper.creatRibbon(RibbonParticleData.getRibbonParticleType(), 40, 0, 0, 0, 1, 255, 255, 255,1,true, true,
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1.0F, 0.0F), false)));
            }
        }
        return IItemExtension.super.onEntitySwing(stack, entity, hand);
    }

    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true)
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof SwordItem && attacker instanceof Player && DoomsGreatsArmor.isAllEquip(attacker)){
            ItemStack driver = GJ.ToItem.getDriver(attacker);
            int time = GJ.ToItem.getNbt(driver).getInt(PlayerEvent.GREATS_HIT_SEGMENT);
            if (time == 3){
                cir.setReturnValue(false);
            }
        }
    }
}
