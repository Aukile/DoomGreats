package com.ankrya.doomsgreats.help;

import com.ankrya.doomsgreats.Config;
import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.client.particle.base.AdvancedParticleBase;
import com.ankrya.doomsgreats.client.particle.base.advanced.AdvancedParticleData;
import com.ankrya.doomsgreats.client.particle.base.advanced.ParticleComponent;
import com.ankrya.doomsgreats.client.particle.base.advanced.RibbonComponent;
import com.ankrya.doomsgreats.client.particle.base.advanced.RibbonParticleData;
import com.ankrya.doomsgreats.client.sound.DelayPlaySound;
import com.ankrya.doomsgreats.message.NMessageCreater;
import com.ankrya.doomsgreats.message.MessageLoader;
import com.ankrya.doomsgreats.message.common.LoopSoundMessage;
import com.ankrya.doomsgreats.message.ex_message.PlayLoopSound;
import com.ankrya.doomsgreats.message.ex_message.StopLoopSound;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
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
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class HTool {

    // 本地相关
    public static void setPersonFront(CameraType cameraType) {
        if (Minecraft.getInstance().options.getCameraType() != cameraType) {
            Minecraft.getInstance().options.setCameraType(cameraType);
        }
    }

    // 世界相关

    public static void playMSound(Level level, double x, double y, double z, String name) {
        SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(name));
        if (soundEvent != null) {
            if (!level.isClientSide()) {
                level.playSound(null, BlockPos.containing(x, y, z), soundEvent, SoundSource.NEUTRAL, 1, 1);
            } else {
                level.playLocalSound(x, y, z, soundEvent, SoundSource.NEUTRAL, 1, 1, false);
            }
        }
    }

    public static List<LivingEntity> rangeFind(Level level, Vec3 center, int radius) {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center)
                        .inflate(radius / 2d), e -> true).stream()
                .sorted(Comparator
                        .comparingDouble(livingEntity ->
                                livingEntity.distanceToSqr(center)))
                .toList();
    }

    private <T extends ParticleOptions> void particle(ServerLevel serverLevel, T pType, Vec3 pos, Vec3 move, double pSpeed, int pParticleCount) {
        serverLevel.sendParticles(pType, pos.x, pos.y, pos.z, pParticleCount, move.x, move.y, move.z, pSpeed);
    }

    // 实体相关

    public static void fixHealth(LivingEntity entity) {
        if (entity.getMaxHealth() < entity.getHealth())
            entity.setHealth(entity.getMaxHealth());
    }

    public static void playExplosionSound(Entity entity) {
        playMSound(entity.level(), entity.getX(), entity.getY() + 1, entity.getZ(), "entity.generic.explode");
    }

    public static void playSound(Player player, String name) {
        playSound(player, name, false);
    }

    public static void playSound(Player player, String name, boolean loop) {
        if (player instanceof ServerPlayer serverPlayer) {
            MessageLoader.sendToPlayersNearby(createLoopSoundMessage(player, name, loop), serverPlayer);
        }
    }

    private static LoopSoundMessage createLoopSoundMessage(Player player, String name, boolean loop) {
        return new LoopSoundMessage(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, name), loop, 16, PlayLoopSound.PLAYERS, player.getId());
    }

    public static void playDelaySound(Player player, String name, boolean loop, int delay) {
        if (player instanceof ServerPlayer serverPlayer) {
            DelayPlaySound.add(serverPlayer, createLoopSoundMessage(player, name, loop), delay);
        }
    }

    public static void cancelDelaySound(Player player, String name) {
        ResourceLocation soundId = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, name);
        DelayPlaySound.cancel(player, soundId);
        HTool.stopSound(player, name);
    }

    public static void stopSound(Player player, String name) {
        if (player instanceof ServerPlayer serverPlayer) {
            MessageLoader.sendToPlayersNearby(
                    new NMessageCreater(new StopLoopSound(player.getId(), PlayLoopSound.PLAYERS
                            , ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, name))), serverPlayer);
        }
    }

    public static boolean hasItem(Entity entity, ItemStack itemstack) {
        if (entity instanceof Player player)
            return player.getInventory().contains(stack -> !stack.isEmpty() && ItemStack.isSameItem(stack, itemstack));
        return false;
    }


    public static void ExplosionTo(Entity source, Entity target, Level world, int amount) {
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

    public static ItemStack getDriver(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.LEGS);
    }

    //数学相关


    public static void transform(Vector3f vector3f, Quaternionf quaternionf) {
        Quaternionf quaternion = new Quaternionf(quaternionf);
        quaternion.mul(new Quaternionf(vector3f.x(), vector3f.y(), vector3f.z(), 0.0F));
        Quaternionf quaternion1 = new Quaternionf(quaternionf);
        quaternion1.conjugate();
        quaternion.mul(quaternion1);
        vector3f.set(quaternion.x(), quaternion.y(), quaternion.z());
    }

    public static void transform(Vector4f vector4f, Matrix4f p_123608_) {
        float f = vector4f.x;
        float f1 = vector4f.y;
        float f2 = vector4f.z;
        float f3 = vector4f.w;
        vector4f.x = p_123608_.m00() * f + p_123608_.m01() * f1 + p_123608_.m02() * f2 + p_123608_.m03() * f3;
        vector4f.y = p_123608_.m10() * f + p_123608_.m11() * f1 + p_123608_.m12() * f2 + p_123608_.m13() * f3;
        vector4f.z = p_123608_.m20() * f + p_123608_.m21() * f1 + p_123608_.m22() * f2 + p_123608_.m23() * f3;
        vector4f.w = p_123608_.m30() * f + p_123608_.m31() * f1 + p_123608_.m32() * f2 + p_123608_.m33() * f3;
    }

    /**
     * {@link AdvancedParticleBase}粒子使用例<br>
     * 我现在还看不懂，长大以后再学
     * @author Aistray
     */
    public static class AdvancedParticleHelper {
        public void addRobbin(Level level, ParticleType<AdvancedParticleData> particle, ParticleType<? extends
                                      RibbonParticleData> ribbon, Vec3 location, float finalTime, float rMax, float rotStartX, float rotEndX,
                              int ribbonLength, float ribbonScale, float firstRot) {
            AdvancedParticleBase.spawnParticle(level, particle, location.x, location.y, location.z, 0, 0, 0, true, 0.0, 0.0, 0.0, 0.0, 0, (double) 245 / 255, (double) 205 / 255, 1, 1, 1, 90, true, true,
                    new ParticleComponent[]{
                            creatOrbit(location, finalTime, rMax, rotStartX, rotEndX, firstRot),
                            creatRibbon(ribbon, ribbonLength, ribbonScale)}
            );
        }

        public void addRobbin(Level level, ParticleType<AdvancedParticleData> particle, ParticleType<? extends
                                      RibbonParticleData> ribbon, Vec3 location, float finalTime, float rMax, float rotStartX, float rotEndX,
                              int ribbonLength, float ribbonScale, float firstRot, double yAdd) {
            AdvancedParticleBase.spawnParticle(level, particle, location.x, location.y, location.z, 0, 0, 0, true, 0.0, 0.0, 0.0, 0.0, 0, (double) 245 / 255, (double) 205 / 255, 1, 1, 1, 90, true, true,
                    new ParticleComponent[]{
                            creatOrbit(location, finalTime, rMax, rotStartX, rotEndX, firstRot, yAdd),
                            creatRibbon(ribbon, ribbonLength, ribbonScale)}
            );
        }

        private RibbonComponent creatRibbon(ParticleType<? extends RibbonParticleData> particle, int length,
                                            float scale) {
            return new RibbonComponent(
                    particle
                    , length, 0.0, 0.0, 0.0, scale, (double) 236 / 255, (double) 204 / 255, 1, 1, true, true
                    , new ParticleComponent[]{
                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1.0F, 0.0F))
                    , new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1.0F, 0.0F), false)
            });
        }

        private ParticleComponent creatOrbit(Vec3 location, float finalTime, float rMax, float rotStartX, float rotEndX,
                                             float firstRot) {
            return new ParticleComponent.Orbit(
                    new Vec3[]{(location)}
                    , ParticleComponent.KeyTrack.startAndEnd(0f, finalTime)
                    , ParticleComponent.KeyTrack.startAndEnd(rMax, 0)
                    , ParticleComponent.KeyTrack.startAndEnd(0, 0)
                    , ParticleComponent.KeyTrack.startAndEnd(rotStartX, rotEndX)
                    , ParticleComponent.KeyTrack.startAndEnd(0, 0)
                    , firstRot
                    , false, 5
            );
        }

        private ParticleComponent creatOrbit(Vec3 location, float finalTime, float rMiddon, float rotStartX,
                                             float rotEndX, float firstRot, double yAdd) {
            return new ParticleComponent.Orbit(
                    new Vec3[]{(location)}
                    , ParticleComponent.KeyTrack.startAndEnd(0f, finalTime)
                    , new ParticleComponent.KeyTrack(new float[]{0, rMiddon, 0}, new float[]{0.0F, 0.5f, 1.0F})
                    , ParticleComponent.KeyTrack.startAndEnd(0, 0)
                    , ParticleComponent.KeyTrack.startAndEnd(rotStartX, rotEndX)
                    , ParticleComponent.KeyTrack.startAndEnd(0, 0)
                    , firstRot
                    , false, yAdd);
        }
    }
}
