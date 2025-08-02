package com.ankrya.doomsgreats.item.base;

import com.ankrya.doomsgreats.item.data.ArmorData;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class BaseRiderArmor extends BaseRiderArmorBase {
    final EquipmentSlot slot;
    public static final DataComponentType<ArmorData> BACKUP_ARMOR =
            DataComponentType.<ArmorData>builder().persistent(ArmorData.CODEC)
                    .networkSynchronized(ArmorData.STREAM_CODEC).build();

    public BaseRiderArmor(Holder<ArmorMaterial> material, Properties properties, EquipmentSlot slot) {
        super(material, getType(slot), properties);
        this.slot = slot;
    }

    public static Type getType(EquipmentSlot slot) {
        return switch (slot){
            case HEAD -> Type.HELMET;
            case CHEST -> Type.CHESTPLATE;
            case LEGS -> Type.LEGGINGS;
            case FEET -> Type.BOOTS;
            default -> throw new IllegalArgumentException("Invalid slot: " + slot);
        };
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (entity instanceof LivingEntity livingEntity){
            if (allArmorEquip(livingEntity)){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, false, false));
                for (Map.Entry<Holder<MobEffect>, Integer> entry : getEffects().entrySet()){
                    if (entry.getKey() == MobEffects.NIGHT_VISION) livingEntity.addEffect(new MobEffectInstance(entry.getKey(), 240, entry.getValue(), false, false));
                    else livingEntity.addEffect(new MobEffectInstance(entry.getKey(), 10, entry.getValue(), false, false));
                }
            } else {
                if (entity instanceof Player player)
                    player.getInventory().clearOrCountMatchingItems(itemStack -> itemStack.is(this), 1, player.getInventory());
                else livingEntity.setItemSlot(slot, ItemStack.EMPTY);
                for (Holder<MobEffect> effect : getEffects().keySet()){
                    livingEntity.removeEffect(effect);
                }
                livingEntity.removeEffect(MobEffects.INVISIBILITY);
            }
        }
    }

    public abstract Map<Holder<MobEffect>, Integer> getEffects();

    public static boolean allArmorEquip(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof BaseRiderArmor
                && entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BaseRiderArmor
                && entity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof BaseDriver
                && entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof BaseRiderArmor;
    }

    // 存储备用盔甲
    public static void storeBackupArmor(ItemStack storageArmor, ItemStack backupArmor) {
        storageArmor.set(BACKUP_ARMOR, ArmorData.fromItemStack(backupArmor));
    }

    // 获取备用盔甲
    public static ItemStack getBackupArmor(ItemStack storageArmor) {
        ArmorData data = storageArmor.get(BACKUP_ARMOR);
        return data != null ? data.toItemStack() : ItemStack.EMPTY;
    }

    public static void equip(LivingEntity entity, EquipmentSlot slot, ItemStack stack){
        ItemStack original = entity.getItemBySlot(slot);
        if (!original.isEmpty()) storeBackupArmor(stack, original);
        if (entity instanceof Player player){
            player.getInventory().armor.set(slot.getIndex(), stack);
            player.getInventory().setChanged();
        } else entity.setItemSlot(slot, stack);
    }

    public static void unequip(LivingEntity entity, EquipmentSlot slot){
        ItemStack stack = entity.getItemBySlot(slot);
        ItemStack backup = getBackupArmor(stack);
        entity.setItemSlot(slot, backup);
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack stack = player.getItemInHand(hand);
//        ItemStack offhandItem = player.getOffhandItem();
//
//        if (hand == InteractionHand.MAIN_HAND && offhandItem.getItem() instanceof ArmorItem) {
//            if (!level.isClientSide) {
//                // 验证盔甲类型
//                EquipmentSlot backupSlot = ((ArmorItem) offhandItem.getItem()).getEquipmentSlot();
//                EquipmentSlot storageSlot = this.getEquipmentSlot();
//
//                if (backupSlot != storageSlot) {
//                    player.displayClientMessage(
//                            Component.literal("备用盔甲必须与主手盔甲类型相同!"),
//                            true
//                    );
//                    return InteractionResultHolder.fail(stack);
//                }
//
//                storeBackupArmor(stack, offhandItem.copy());
//                offhandItem.shrink(1);
//                player.displayClientMessage(
//                        Component.literal("备用盔甲已存储!"),
//                        true
//                );
//
//                // 触发更新
//                player.inventoryMenu.broadcastChanges();
//            }
//            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
//        }
//        return super.use(level, player, hand);
//    }
}
