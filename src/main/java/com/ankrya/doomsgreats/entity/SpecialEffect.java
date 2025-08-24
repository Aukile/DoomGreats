package com.ankrya.doomsgreats.entity;

import com.ankrya.doomsgreats.init.ClassRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;

public class SpecialEffect extends Entity implements GeoEntity {
    public static final EntityDataAccessor<Integer> DEAD_TIME = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> AUTO_CLEAR = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> MODEL = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.OPTIONAL_UUID);
    public Player owner;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public SpecialEffect(EntityType<?> type, Level level) {
        this(type, level, null, null, null, 0);
    }

    public SpecialEffect(EntityType<?> type, Level level, Player owner, String model, String texture, int dead) {
        super(type, level);
        if (dead != 0) this.entityData.set(DEAD_TIME, dead);
        if (model != null)this.entityData.set(MODEL, model);
        if (texture != null)this.entityData.set(TEXTURE, texture);
        this.owner = owner;
        if (owner != null){
            this.setOwnerUUID(owner.getUUID());
        }
    }

    public SpecialEffect(Level level, Player owner, String model, String texture, int dead) {
        this(ClassRegister.getRegisterObject("henshin_effect", EntityType.class).get(), level, owner, model, texture, dead);
    }

    public void positionSet(Entity entity) {
        this.positionSet(entity, Entity::setPos);
    }
    protected void positionSet(Entity entity, MoveFunction function) {
        if(this.getOwner() != null) {
            LivingEntity livingEntity = this.getOwner();
            double ny = livingEntity.getY() - 0.136;

            entity.setYRot(livingEntity.getYRot());
            entity.setXRot(livingEntity.getXRot());
            entity.yRotO = entity.getYRot();
            entity.xRotO = entity.getXRot();

            function.accept(entity, livingEntity.getX(), ny, livingEntity.getZ());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DEAD_TIME, 20);
        builder.define(AUTO_CLEAR, true);
        builder.define(ANIMATION, "idle");
        builder.define(TEXTURE, "null");
        builder.define(MODEL, "null");
        builder.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
//        super.readAdditionalSaveData(tag);
        if (tag.contains("deadTime"))
            this.entityData.set(DEAD_TIME, tag.getInt("deadTime"));
        if (tag.contains("autoClear"))
            this.entityData.set(AUTO_CLEAR, tag.getBoolean("autoClear"));
        if (tag.contains("animation"))
            this.entityData.set(ANIMATION, tag.getString("animation"));
        if (tag.contains("texture"))
            this.entityData.set(TEXTURE, tag.getString("texture"));
        if (tag.contains("model"))
            this.entityData.set(MODEL, tag.getString("model"));
        if (tag.contains("owner_uuid")) {
            this.entityData.set(OWNER_UUID, Optional.of(tag.getUUID("owner_uuid")));
            this.owner = null;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("deadTime", this.entityData.get(DEAD_TIME));
        tag.putBoolean("autoClear", this.entityData.get(AUTO_CLEAR));
        tag.putString("animation", this.entityData.get(ANIMATION));
        tag.putString("texture", this.entityData.get(TEXTURE));
        tag.putString("model", this.entityData.get(MODEL));
        tag.putUUID("owner_uuid", this.entityData.get(OWNER_UUID).get());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance <= 256D;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if(getDeadTime() != 999999){
            if(getDeadTime() > 0){
                setDeadTime(getDeadTime() - 1);
            }else{
                this.discard();
            }
        }
        LivingEntity owner = getOwner();
        if (AutoClear() && (owner == null || !owner.isAlive()) )
            this.discard();
        else  {
            this.positionSet(this);
        }
    }

    private PlayState predicate(AnimationState<SpecialEffect> state) {
        String animation = this.animationName();
        if (animation.equals("null"))
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        else {
            state.getController().setAnimation(RawAnimation.begin().then(animation, Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,"idle",this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public int getDeadTime(){
        return this.entityData.get(DEAD_TIME);
    }

    public void setDeadTime(int num){
        this.entityData.set(DEAD_TIME,num);
    }

    public String model(){
        return this.entityData.get(MODEL);
    }

    public String animationName(){
        return this.entityData.get(ANIMATION);
    }

    public String texture(){
        return this.entityData.get(TEXTURE);
    }

    public void setAnimationName(String animation){
        this.entityData.set(ANIMATION,animation);
    }

    public void setTexture(String texture){
        this.entityData.set(TEXTURE,texture);
    }

    public void setAutoClear(boolean flag){
        this.entityData.set(AUTO_CLEAR,flag);
    }

    public void setOwnerUUID(UUID uuid){
        this.entityData.set(OWNER_UUID,Optional.of(uuid));
    }

    public boolean AutoClear(){
        return this.entityData.get(AUTO_CLEAR);
    }

    public LivingEntity getOwner() {
        UUID uuid = this.entityData.get(OWNER_UUID).orElse(null);
        if (this.owner != null)
            return this.owner;
        else if (uuid != null)
            return this.level().getPlayerByUUID(uuid);
        else return null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
        builder = builder.add(Attributes.MAX_HEALTH, 2);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
        return builder;
    }
}
