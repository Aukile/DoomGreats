package com.ankrya.doomsgreats.entity;

import com.ankrya.doomsgreats.init.ClassRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SpecialEffect extends Entity implements GeoEntity {
    public static final EntityDataAccessor<Integer> DEAD_TIME = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> AUTO_CLEAR = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> MODEL = SynchedEntityData.defineId(SpecialEffect.class, EntityDataSerializers.STRING);
    public LivingEntity owner;
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public SpecialEffect(EntityType<?> type, Level level) {
        super(type, level);
    }

    public SpecialEffect(Level level, LivingEntity owner, String model, String texture, int dead) {
        this(ClassRegister.getRegisterObject("henshin_effect", EntityType.class).get(), level);
        this.entityData.set(DEAD_TIME, dead);
        this.entityData.set(MODEL, model);
        this.entityData.set(TEXTURE, texture);
        this.owner = owner;
    }

    public SpecialEffect doomsEffect(Level level, LivingEntity owner){
        return new SpecialEffect(level, owner, "dooms_geatshenxin", "dooms_greatshenxin", (int) (25.08 * 20));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DEAD_TIME, 20);
        builder.define(AUTO_CLEAR, true);
        builder.define(ANIMATION, "idle");
        builder.define(TEXTURE, "null");
        builder.define(MODEL, "null");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
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
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("deadTime", this.entityData.get(DEAD_TIME));
        tag.putBoolean("autoClear", this.entityData.get(AUTO_CLEAR));
        tag.putString("animation", this.entityData.get(ANIMATION));
        tag.putString("texture", this.entityData.get(TEXTURE));
        tag.putString("model", this.entityData.get(MODEL));
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
        if (AutoClear() && (this.getOwner() == null || this.getVehicle() != this.getOwner()) )
            this.discard();
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

    public boolean AutoClear(){
        return this.entityData.get(AUTO_CLEAR);
    }

    public LivingEntity getOwner() {
        return owner;
    }
}
