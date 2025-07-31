package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.entity.SpecialEffect;
import com.ankrya.doomsgreats.init.assist.RegisterAssist;
import com.ankrya.doomsgreats.item.DesireDriver;
import com.ankrya.doomsgreats.item.DoomGreatsArmor;
import com.ankrya.doomsgreats.item.base.BaseRiderArmor;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClassRegister {
    private static final String modid = DoomsGreats.MODID;
    public static Map<Class<?>,  DeferredRegister<?>> registers = new HashMap<>();
    public static Map<Class<?>, Map<String, Supplier<?>>> registerObjects = new HashMap<>();
//    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, modid);
//    public static final Supplier<Item> LOGO = REGISTER.register("logo", () -> new Item(new Item.Properties()));

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

    public static <T> void updateRegisters(Class<? extends T> type, final String name, Supplier<?> object){
        if (registerObjects.containsKey(type)){
            Map<String, Supplier<?>> supplierMap = registerObjects.get(type);
            supplierMap.put(name, object);
        }else {
            Map<String, Supplier<?>> supplierMap = new HashMap<>();
            supplierMap.put(name, object);
            registerObjects.put(type, supplierMap);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Supplier<T> register(Class<? extends T> type, final String name, final Supplier<? extends T> sup){
        DeferredRegister<T> r = (DeferredRegister<T>) getRegisterSource(type);
        Supplier<T> object = r.register(name, sup);
        updateRegisters(type, name, object);
        return object;
    }

    @SuppressWarnings("unchecked")
    public static <T> DeferredHolder<T, T> registerAsHolder(Class<? extends T> type, final String name, final Supplier<? extends T> sup){
        DeferredRegister<T> r = (DeferredRegister<T>) getRegisterSource(type);
        DeferredHolder<T, T> object = r.register(name, sup);
        updateRegisters(type, name, object);
        return object;
    }

    @ApiStatus.Internal
    public static <T> Supplier<T> easyRegister(Class<? extends T> type, final String name, final Supplier<? extends T> sup){
        registerSource(type);
        return register(type, name, sup);
    }

    @SuppressWarnings("unchecked")
    public static <T> Supplier<T> getRegisterObject(String name, Class<T> clazz){
        Map<String, Supplier<?>> map = registerObjects.get(clazz);
        return (Supplier<T>) map.get(name);
    }

    public static Map<String, Supplier<?>> getRegisterObjects(Class<?> clazz){
        return registerObjects.get(clazz);
    }

    public static boolean isRegistered(Class<?> clazz){
        return registers.containsKey(clazz);
    }

    public static void init(IEventBus bus){
        useRegister();
        for (DeferredRegister<?> register : registers.values()){
            register.register(bus);
        }
    }

    public static void useRegister(){
//        注册声音
        registerSource(SoundEvent.class);
        soundRegister("desire_driver", "doomsgeatsbuckleopen", "doomsgeatssetjudgment", "doomsgeatshenshinfull", "gun", "blade", "revolve_on");

//        注册实体
        Class<?> entityType = EntityType.class;
        registerSource(entityType);
        register(entityType, "henshin_effect", () -> EntityType.Builder.of(SpecialEffect::new, MobCategory.MISC).sized(0.0F, 0.0F).setShouldReceiveVelocityUpdates(true).updateInterval(3).build("effects"));

//        注册物品
        Class<Item> item = Item.class;
        registerSource(item);
        register(item, "desire_driver", () -> new DesireDriver(new Item.Properties()));
        register(item, "logo", () -> new Item(new Item.Properties()));
        for (EquipmentSlot slot : BaseRiderArmor.getSlots())
            register(item, "dooms_greats_" + slot.getName(), () -> new DoomGreatsArmor(new Item.Properties(), slot));

//        注册创造物品栏
        easyRegister(CreativeModeTab.class, "dooms_greats_tab", () -> CreativeModeTab.builder().icon(() -> ClassRegister.getRegisterObject("logo", Item.class).get().getDefaultInstance())
                .title(Component.translatable("item_group.dooms_greats.dooms_greats_tab")).displayItems((parameters, output) -> {
            output.accept(ClassRegister.getRegisterObject("desire_driver", Item.class).get());
        }).build());
    }

    private static void soundRegister(String... names){
        for (String name : names)
              register(SoundEvent.class, name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, name)));
    }
}
