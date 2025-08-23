package com.ankrya.doomsgreats.help;

import com.ankrya.doomsgreats.Config;
import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.client.LoopSound;
import com.ankrya.doomsgreats.message.NMessageCreater;
import com.ankrya.doomsgreats.message.MessageLoader;
import com.ankrya.doomsgreats.message.common.LoopSoundMessage;
import com.ankrya.doomsgreats.message.ex_message.PlayLoopSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class HTool {

    // 世界相关

    public static final Minecraft mc = Minecraft.getInstance();

    public static Level getLocalLevel(){
        return mc.level;
    }

    public static void playMSound(Level level, double x, double y, double z, String name){
        SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(name));
        if (soundEvent != null){
            if (!level.isClientSide()) {
                level.playSound(null, BlockPos.containing(x, y, z), soundEvent, SoundSource.NEUTRAL, 1, 1);
            } else {
                level.playLocalSound(x, y, z, soundEvent, SoundSource.NEUTRAL, 1, 1, false);
            }
        }
    }

    public static List<LivingEntity> rangeFind(Level level, Vec3 center, int radius){
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center)
                        .inflate(radius / 2d), e -> true).stream()
                .sorted(Comparator
                        .comparingDouble(livingEntity ->
                                livingEntity.distanceToSqr(center)))
                .toList();
    }

    // 实体相关

    public static Optional<Player> getLocalPlayer(){
        return Optional.ofNullable(mc.player);
    }

    public static void fixHealth(LivingEntity entity){
        if (entity.getMaxHealth() < entity.getHealth())
            entity.setHealth(entity.getMaxHealth());
    }

    public static void playExplosionSound(Entity entity){
        playMSound(entity.level(), entity.getX(), entity.getY() + 1, entity.getZ(), "entity.generic.explode");
    }

    public static void playSound(Player player, String name){
        if (player instanceof ServerPlayer serverPlayer){
            MessageLoader.sendToPlayer(
                    new LoopSoundMessage(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, name)
                            , false, 10, PlayLoopSound.PLAYERS, player.getId()), serverPlayer);
        }
    }

    public static void ExplosionTo(Entity source, Entity target, Level world, int amount){
        playExplosionSound(target);
        target.hurt(new DamageSource(world.holderOrThrow(DamageTypes.PLAYER_EXPLOSION), source), amount * Config.ATTACK_BASE.get());
        if (world instanceof ServerLevel level)
            level.sendParticles(ParticleTypes.EXPLOSION, (target.getX()), (target.getY() + 1), (target.getZ()), 2, 0.1, 0.1, 0.1, 0.1);
    }

    public static boolean isFront(Entity entity, Entity target, float width) {
        return (target.getX() - entity.getX()) * entity.getLookAngle().x >= 0 && (target.getZ() - entity.getZ()) * entity.getLookAngle().z >= width;
    }

    public static List<LivingEntity> rangeFind(Entity entity, double radius) {
        return rangeFind(entity.level(), entity.position(), (int) radius);
    }

    //物品相关

    public static ItemStack getDriver(LivingEntity entity){
        return entity.getItemBySlot(EquipmentSlot.LEGS);
    }
}
