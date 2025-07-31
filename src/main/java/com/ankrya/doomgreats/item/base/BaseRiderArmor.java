package com.ankrya.doomgreats.item.base;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BaseRiderArmor extends BaseGeoArmor{
    final EquipmentSlot slot;
    public static final DataComponentType<ItemStack> BACKUP_ARMOR =
            DataComponentType.<ItemStack>builder().persistent(ItemStack.CODEC)
                    .networkSynchronized(ItemStack.STREAM_CODEC).build();

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
        if (entity instanceof LivingEntity livingEntity && !allArmorEquip(livingEntity)){
            if (entity instanceof Player player)
                player.getInventory().clearOrCountMatchingItems(itemStack -> itemStack.is(this), 1, player.getInventory());
            else livingEntity.setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    public static boolean allArmorEquip(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof BaseRiderArmor
                && entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BaseRiderArmor
                && entity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof BaseDriver
                && entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof BaseRiderArmor;
    }

    // 存储备用盔甲
    public static void storeBackupArmor(ItemStack storageArmor, ItemStack backupArmor) {
        storageArmor.set(BACKUP_ARMOR, backupArmor);
    }

    // 获取备用盔甲
    public static ItemStack getBackupArmor(ItemStack storageArmor) {
        return storageArmor.getOrDefault(BACKUP_ARMOR, ItemStack.EMPTY);
    }

    public static void equip(LivingEntity entity, EquipmentSlot slot, ItemStack stack){
        ItemStack original = entity.getItemBySlot(slot);
        storeBackupArmor(stack, original);
        entity.setItemSlot(slot, stack);
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
