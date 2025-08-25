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

/**
 * 工具箱<br>
 * 怕以后多了找不到我做了分类<br>
 * 不好分类的放外面了
 */
public final class HTool {

    /**
     * 本地相关
     */
    public static abstract class Local {
        public static void setPersonFront(CameraType cameraType) {
            if (Minecraft.getInstance().options.getCameraType() != cameraType) {
                Minecraft.getInstance().options.setCameraType(cameraType);
            }
        }
    }

    /**
     * 世界相关
     */
    public static abstract class World {
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
    }

    public static boolean isFront(Entity entity, Entity target, float width) {
        return (target.getX() - entity.getX()) * entity.getLookAngle().x >= 0 && (target.getZ() - entity.getZ()) * entity.getLookAngle().z >= width;
    }

    public static List<LivingEntity> rangeFind(Entity entity, double radius) {
        return World.rangeFind(entity.level(), entity.position(), (int) radius);
    }

    /**
     * 玩家相关
     */
    public static abstract class ToPlayer {
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
            HTool.ToPlayer.stopSound(player, name);
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
    }

    /**
     * 实体相关
     */
    public static abstract class ToEntity {
        public static void fixHealth(LivingEntity entity) {
            if (entity.getMaxHealth() < entity.getHealth())
                entity.setHealth(entity.getMaxHealth());
        }

        public static void playExplosionSound(Entity entity) {
            World.playMSound(entity.level(), entity.getX(), entity.getY() + 1, entity.getZ(), "entity.generic.explode");
        }
    }


    /**
     * 物品相关
     */
    public static abstract class ToItem {
        public static ItemStack getDriver(LivingEntity entity) {
            return entity.getItemBySlot(EquipmentSlot.LEGS);
        }
    }

    /**
     * 数学相关
     */
    public static abstract class ToMath {
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
    }

    /**
     * 粒子相关
     */
    public static abstract class ToParticle {
        public static <T extends ParticleOptions> void particle(ServerLevel serverLevel, T pType, Vec3 pos, Vec3 move, double pSpeed, int pParticleCount) {
            serverLevel.sendParticles(pType, pos.x, pos.y, pos.z, pParticleCount, move.x, move.y, move.z, pSpeed);
        }

        public static void ExplosionTo(Entity source, Entity target, Level world, int amount) {
            ToEntity.playExplosionSound(target);
            target.hurt(new DamageSource(world.holderOrThrow(DamageTypes.PLAYER_EXPLOSION), source), amount * Config.ATTACK_BASE.get());
            if (world instanceof ServerLevel level)
                level.sendParticles(ParticleTypes.EXPLOSION, (target.getX()), (target.getY() + 1), (target.getZ()), 2, 0.1, 0.1, 0.1, 0.1);
        }
    }

    /**
     * {@link AdvancedParticleBase}粒子使用例<br>
     *
     * @author Aistray
     */
    public static abstract class AdvancedParticleHelper {

        public static void addRobbin(Level world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent... components) {
            AdvancedParticleBase.spawnParticle(world, particle, x, y, z, motionX, motionY, motionZ, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, r, g, b, a, drag, duration, emissive, canCollide, components);
        }

        /**
         * 创建一个带拖尾的粒子<br>
         * 此方法为创建例<br>
         * ps:好可怕，35个参数，写死你<br>
         * ps:Is never over~
         * @param world           世界
         * @param particle        粒子类型
         * @param ribbon          拖尾粒子类型
         * @param x               坐标X
         * @param y               坐标Y
         * @param z               坐标Z
         * @param motionX         粒子运动X
         * @param motionY         粒子运动Y
         * @param motionZ         粒子运动Z
         * @param faceCamera      是否面向摄像机
         * @param yaw             粒子旋转Y
         * @param pitch           粒子旋转X
         * @param roll            粒子旋转Z
         * @param faceCameraAngle 粒子面向摄像机角度
         * @param scale           粒子大小
         * @param r               粒子颜色R
         * @param g               粒子颜色G
         * @param b               粒子颜色B
         * @param a               粒子透明度
         * @param drag            粒子运动速度
         * @param duration        粒子生命时长
         * @param emissive        粒子是否发光
         * @param canCollide      粒子是否可碰撞
         * @param startTime       粒子开始运动时间
         * @param finalTime       粒子结束运动时间
         * @param rMin            粒子运动速度最小值
         * @param rMax            粒子运动速度最大值
         * @param rotStartX       粒子运动旋转X开始
         * @param rotEndX         粒子运动旋转X结束
         * @param rotStartY       粒子运动旋转Y开始
         * @param rotEndY         粒子运动旋转Y结束
         * @param rotStartZ       粒子运动旋转Z开始
         * @param rotEndZ         粒子运动旋转Z结束
         * @param firstRot        粒子运动旋转X开始
         * @param yAdd            粒子运动Y轴偏移
         * @param length          粒子拖尾长度
         */
        public static void addRobbin(Level world, ParticleType<AdvancedParticleData> particle, ParticleType<RibbonParticleData> ribbon, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide
                , float startTime, float finalTime, float rMin, float rMax, float rotStartX, float rotEndX, float rotStartY, float rotEndY, float rotStartZ, float rotEndZ, float firstRot, double yAdd
                , int length) {
            addRobbin(world, particle, x, y, z, motionX, motionY, motionZ, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, r, g, b, a, drag, duration, emissive, canCollide
                    , creatOrbit(new Vec3(x, y, z), startTime, finalTime, rMin, rMax, rotStartX, rotEndX, rotStartY, rotEndY, rotStartZ, rotEndZ, firstRot, faceCamera, yAdd)
                    , creatRibbon(ribbon, length, yaw, pitch, roll, scale, r, g, b, a, true, emissive));
        }

        /**
         * 演示使用
         */
        @Deprecated
        public static void addRobbin(Level level, ParticleType<AdvancedParticleData> particle, ParticleType<? extends
                                             RibbonParticleData> ribbon, Vec3 location, float finalTime, float rMax, float rotStartX, float rotEndX,
                                     int ribbonLength, float ribbonScale, float firstRot, double yAdd) {
            AdvancedParticleBase.spawnParticle(level, particle, location.x, location.y, location.z, 0, 0, 0, true, 0.0, 0.0, 0.0, 0.0, 1, (double) 245 / 255, (double) 205 / 255, 1, 1, 1, 45, true, true,
                    new ParticleComponent[]{
                            creatOrbit(location, finalTime, rMax, rotStartX, rotEndX, firstRot, yAdd),
                            creatRibbon(ribbon, ribbonLength, ribbonScale)}
            );
        }

        /**
         * @param particle   粒子类型
         * @param length     拖尾粒子长度
         * @param yaw        粒子旋转Y
         * @param pitch      粒子旋转X
         * @param roll       粒子旋转Z
         * @param scale      粒子大小
         * @param r          粒子颜色R
         * @param g          粒子颜色G
         * @param b          粒子颜色B
         * @param a          粒子透明度
         * @param faceCamera 是否面向摄像机
         * @param emissive   粒子是否发光
         * @param components 组件
         * @return 拖尾粒子的创建
         */
        public static RibbonComponent creatRibbon(ParticleType<? extends RibbonParticleData> particle, int length, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, boolean faceCamera, boolean emissive, ParticleComponent... components) {
            return new RibbonComponent(particle, length, yaw, pitch, roll, scale, r / 255, g / 255, b / 255, a, faceCamera, emissive, components);
        }

        /**
         * 默认组件版
         *
         * @param particle   粒子类型
         * @param length     拖尾粒子长度
         * @param yaw        粒子旋转Y
         * @param pitch      粒子旋转X
         * @param roll       粒子旋转Z
         * @param scale      粒子大小
         * @param r          粒子颜色R
         * @param g          粒子颜色G
         * @param b          粒子颜色B
         * @param a          粒子透明度
         * @param faceCamera 是否面向摄像机
         * @param emissive   粒子是否发光
         * @return 拖尾粒子的创建
         */
        public static RibbonComponent creatRibbon(ParticleType<? extends RibbonParticleData> particle, int length, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, boolean faceCamera, boolean emissive) {
            return creatRibbon(particle, length, yaw, pitch, roll, scale, r, g, b, a, faceCamera, emissive
                    , new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1.0F, 0.0F))
                    , new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1.0F, 0.0F), false));
        }

        //使用例
        private static RibbonComponent creatRibbon(ParticleType<? extends RibbonParticleData> particle, int length, float scale) {
            return creatRibbon(particle, length, 0.0, 0.0, 0.0, scale, (double) 236, (double) 204, 255, 1, true, true);
        }

        /**
         * 创建一个旋转的粒子
         *
         * @param location   粒子位置
         * @param startTime  粒子开始时间
         * @param finalTime  粒子结束时间
         * @param rMin       粒子最小半径
         * @param rMax       粒子最大半径
         * @param rotStartX  粒子旋转起始角度X
         * @param rotEndX    粒子旋转结束角度X
         * @param rotStartY  粒子旋转起始角度Y
         * @param rotEndY    粒子旋转结束角度Y
         * @param rotStartZ  粒子旋转起始角度Z
         * @param rotEndZ    粒子旋转结束角度Z
         * @param firstRot   粒子旋转起始角度
         * @param faceCamera 是否面向摄像机
         * @param yAdd       粒子位置Y轴偏移
         */
        public static ParticleComponent creatOrbit(Vec3 location, float startTime, float finalTime, float rMin, float rMax, float rotStartX, float rotEndX, float rotStartY, float rotEndY, float rotStartZ, float rotEndZ, float firstRot, boolean faceCamera, double yAdd) {
            return creatOrbit(location
                    , ParticleComponent.KeyTrack.startAndEnd(startTime, finalTime)
                    , ParticleComponent.KeyTrack.startAndEnd(rMin, rMax)
                    , ParticleComponent.KeyTrack.startAndEnd(rotStartX, rotEndX)
                    , ParticleComponent.KeyTrack.startAndEnd(rotStartY, rotEndY)
                    , ParticleComponent.KeyTrack.startAndEnd(rotStartZ, rotEndZ)
                    , firstRot, faceCamera, yAdd);
        }

        /**
         * 创建一个旋转的粒子
         *
         * @param location   粒子位置
         * @param timePeriod 时间周期
         * @param revolve    粒子旋转半径
         * @param xMove      粒子位置X轴偏移
         * @param yMove      粒子位置Y轴偏移
         * @param zMove      粒子位置Z轴偏移
         * @param firstRot   粒子旋转起始角度
         * @param faceCamera 是否面向摄像机
         * @param yAdd       粒子位置Y轴偏移
         */
        public static ParticleComponent creatOrbit(Vec3 location, ParticleComponent.KeyTrack timePeriod, ParticleComponent.KeyTrack revolve, ParticleComponent.KeyTrack xMove, ParticleComponent.KeyTrack yMove, ParticleComponent.KeyTrack zMove, float firstRot, boolean faceCamera, double yAdd) {
            return new ParticleComponent.Orbit(new Vec3[]{(location)}
                    , timePeriod, revolve, xMove, yMove, zMove
                    , firstRot, faceCamera, yAdd);
        }

        //使用例
        private static ParticleComponent creatOrbit(Vec3 location, float finalTime, float rMiddon, float rotStartX,
                                                    float rotEndX, float firstRot, double yAdd) {
            return creatOrbit(location
                    , ParticleComponent.KeyTrack.startAndEnd(0f, finalTime)
                    , new ParticleComponent.KeyTrack(new float[]{0, rMiddon, 0}, new float[]{0.0F, 0.5f, 1.0F})
                    , ParticleComponent.KeyTrack.startAndEnd(0, 0)
                    , ParticleComponent.KeyTrack.startAndEnd(rotStartX, rotEndX)
                    , ParticleComponent.KeyTrack.startAndEnd(0, 0)
                    , firstRot, false, yAdd);
        }
    }
}
