package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.entity.DoomsEffect;
import com.ankrya.doomsgreats.entity.SpecialEffect;
import com.ankrya.doomsgreats.init.assist.RegisterAssist;
import com.ankrya.doomsgreats.item.DesireDriver;
import com.ankrya.doomsgreats.item.DoomsGreatsArmor;
import com.ankrya.doomsgreats.item.GoldenGeatsBusterQB9;
import com.ankrya.doomsgreats.item.LogoItem;
import com.ankrya.doomsgreats.item.base.armor.BaseRiderArmor;
import com.ankrya.doomsgreats.item.base.armor.BaseRiderArmorBase;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ClassRegister {
    private static final String modid = DoomsGreats.MODID;
    public static Map<Class<?>,  DeferredRegister<?>> registers = new HashMap<>();
    public static Map<Class<?>, Map<String, Supplier<?>>> registerObjects = new HashMap<>();

    public static <T> DeferredRegister<T> registerSource(Class<T> type, String registerName) {
        return registerSource(type, registerName, modid);
    }

    public static <T> void registerSource(Class<T> type, ResourceKey<Registry<T>> registerTo) {
        registerSource(type, registerTo, modid);
    }

    public static <T> DeferredRegister<T> registerSource(Class<T> type, String registerName, String modid) {
        ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(ResourceLocation.parse(registerName));
        return registerSource(type, key, modid);
    }

    public static <T> DeferredRegister<T> registerSource(Class<T> type, ResourceKey<Registry<T>> registerTo, String modid) {
        DeferredRegister<T> register = DeferredRegister.create(registerTo, modid);
        if (!registers.containsKey(type) && RegisterAssist.registerSourceSafe(type, registers)){
            registers.put(type, register);
        }
        return register;
    }

    @ApiStatus.Internal
    public static <T> DeferredRegister<T> registerSource(Class<T> type){
        return registerSource(type, RegisterAssist.getRegisterName(type));
    }

    public static DeferredRegister<?> getRegisterSource(Class<?> type){
        return registers.get(type);
    }

    /**
     * 这是中间用的方法，不是更新的方法，请勿调用
     */
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

    public static <T> DeferredRegister<? extends T> onceRegister(Class<? extends T> type, final String name, final Supplier<? extends T> sup){
        DeferredRegister<? extends T> source = registerSource(type);
        register(type, name, sup);
        return source;
    }

    public static <T> void easyRegister(Class<T> clazz, IEventBus bus, Tectonic<T>[] tectonics){
        DeferredRegister<T> source = registerSource(clazz);
        for (Tectonic<T> tectonic : tectonics) {
            register(clazz, tectonic.name(), tectonic::t);
        }
        source.register(bus);
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

    /**
     *神人注册机，如是说，虽然导致优先级方面有问题
     *但是豪玩！
     * 注册去static里面注册吧
     * <p>
     * @see ClassRegister#registerSource 注册注册源
     * @see ClassRegister#register 注册对象
     * <p>
     * 使用例
     * @see ClassRegister#getRegisterObject 获取注册对象，加一个.get()获取实例
     */
    public static void init(IEventBus bus){
        for (DeferredRegister<?> register : registers.values()){
            register.register(bus);
        }
    }


    static {
//        注册声音
        registerSource(SoundEvent.class);
        soundRegister("desire_driver", "doomsgeatsbuckleopen", "doomsgeatssetjudgment", "doomsgeatshenshinfull", "gun", "blade", "revolve_on");

//        注册实体
        Class<?> entityType = EntityType.class;
        registerSource(entityType);
        register(entityType, "henshin_effect", () -> EntityType.Builder.of(SpecialEffect::new, MobCategory.MISC).sized(0.0F, 0.0F).setShouldReceiveVelocityUpdates(true).updateInterval(3).build("effects"));
        register(entityType, "dooms_effect", () -> EntityType.Builder.of(DoomsEffect::new, MobCategory.MISC).sized(0.0F, 0.0F).setShouldReceiveVelocityUpdates(true).updateInterval(3).build("dooms_effect"));

//        注册盔甲材质
//        Class<ArmorMaterial> armorMaterial = ArmorMaterial.class;
//        registerSource(armorMaterial);
//        register(armorMaterial, "dooms_greats_material", GreatsArmorMaterial::doomsGreatsArmor);
//        register(armorMaterial, "dgp_blank_material", GreatsArmorMaterial::dgpBlank);

//        注册物品
        Class<Item> item = Item.class;
        registerSource(item);
        register(item, "desire_driver", () -> new DesireDriver(new Item.Properties().stacksTo(1)));
        register(item, "logo", () -> new LogoItem(new Item.Properties().stacksTo(1)));
        for (EquipmentSlot slot : BaseRiderArmorBase.getSlots())
            register(item, "dooms_greats_" + slot.getName(), () -> new DoomsGreatsArmor(new Item.Properties().stacksTo(1), slot));
        register(item, "buster_qb_9_sword", GoldenGeatsBusterQB9::new);

        Class<?> dataComponentType = DataComponentType.class;
        registerSource(dataComponentType);
        register(dataComponentType, "dooms_greats_data_backup", () -> BaseRiderArmor.BACKUP_ARMOR);

//        注册创造物品栏
        onceRegister(CreativeModeTab.class, "dooms_greats_tab", () -> CreativeModeTab.builder().icon(() -> ClassRegister.getRegisterObject("logo", Item.class).get().getDefaultInstance())
                .title(Component.translatable("item_group.dooms_greats.dooms_greats_tab")).displayItems((parameters, output) -> {
            output.accept(ClassRegister.getRegisterObject("desire_driver", Item.class).get());
        }).build());
    }

    private static void soundRegister(String... names){
        for (String name : names)
              register(SoundEvent.class, name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, name)));
    }

    public record Tectonic<T>(String name, T t) {}
}
