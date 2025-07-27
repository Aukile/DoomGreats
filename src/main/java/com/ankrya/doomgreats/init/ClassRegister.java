package com.ankrya.doomgreats.init;

import com.ankrya.doomgreats.DoomGreats;
import com.ankrya.doomgreats.entity.SpecialEffect;
import com.ankrya.doomgreats.init.assist.RegisterAssist;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClassRegister {
    private static final String modid = DoomGreats.MODID;
    public static Map<Class<?>,  DeferredRegister<?>> registers = new HashMap<>();
    public static Map<Class<?>, Map<String, Supplier<?>>> registerObjects = new HashMap<>();

    public static <T> void registerSource(Class<T> type, String registerName) {
        registerSource(type, registerName, modid);
    }

    public static <T> void registerSource(Class<T> type, ResourceKey<Registry<T>> registerTo) {
        registerSource(type, registerTo, modid);
    }

    public static <T> void registerSource(Class<T> type, String registerName, String modid) {
        ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(ResourceLocation.parse(registerName));
        registerSource(type, key, modid);
    }

    public static <T> void registerSource(Class<T> type, ResourceKey<Registry<T>> registerTo, String modid) {
        DeferredRegister<T> register = DeferredRegister.create(registerTo, modid);
        if (!registers.containsKey(type) && RegisterAssist.registerSourceSafe(type, registers)){
            registers.put(type, register);
        }
    }

    public static void registerSource(Class<?> type){
        registerSource(type, RegisterAssist.getRegisterName(type));
    }

    public static DeferredRegister<?> getRegisterSource(Class<?> type){
        return registers.get(type);
    }

    public static <T> Supplier<T> register(Class<? extends T> type, final String name, final Supplier<? extends T> sup){
        DeferredRegister<T> r = (DeferredRegister<T>) getRegisterSource(type);
        Supplier<T> object = r.register(name, sup);
        if (registerObjects.containsKey(type)){
            Map<String, Supplier<?>> supplierMap = registerObjects.get(type);
            supplierMap.put(name, object);
            registerObjects.put(type, supplierMap);
        }else {
            registerObjects.put(type, Map.of(name, object));
        }
        return object;
    }

    @ApiStatus.Internal
    public static <T> Supplier<T> easyRegister(Class<? extends T> type, final String name, final Supplier<? extends T> sup){
        registerSource(type);
        return register(type, name, sup);
    }

    public static Supplier<?> getRegisterObject(String name, Class<?> clazz){
        Map<String, Supplier<?>> map = registerObjects.get(clazz);
        return map.get(name);
    }

    public static void init(IEventBus bus){
        useRegister();
        for (DeferredRegister<?> register : registers.values()){
            register.register(bus);
        }
    }

    public static void useRegister(){
//        注册创造物品栏
        easyRegister(CreativeModeTab.class, "doom_greats", () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.GLASS)).build());



//        注册实体
        registerSource(EntityType.class);
        register(EntityType.class, "henshin_effect", () -> EntityType.Builder.of(SpecialEffect::new, MobCategory.MISC).sized(0.0F, 0.0F).setShouldReceiveVelocityUpdates(true).updateInterval(3).build("effects"));


//        注册声音
        registerSource(SoundEvent.class);
        soundRegister("desire_driver", "doomsgeatsbuckleopen", "doomsgeatssetjudgment", "doomsgeatshenshinfull", "gun", "blade", "revolve_on");
    }

    private static void soundRegister(String... names){
        for (String name : names)
            register(SoundEvent.class, name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DoomGreats.MODID, name)));
    }
}
