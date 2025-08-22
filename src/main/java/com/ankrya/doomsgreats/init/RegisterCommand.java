package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.compat.animation.PlayerAnimator;
import com.ankrya.doomsgreats.help.ItemHelp;
import com.ankrya.doomsgreats.help.runnable.WaitToRun;
import com.ankrya.doomsgreats.item.DesireDriver;
import com.ankrya.doomsgreats.item.DoomsGreatsArmor;
import com.ankrya.doomsgreats.interfaces.IGeoItem;
import com.ankrya.doomsgreats.item.base.armor.BaseRiderArmor;
import com.ankrya.doomsgreats.item.base.armor.BaseRiderArmorBase;
import com.ankrya.doomsgreats.message.MessageLoader;
import com.ankrya.doomsgreats.message.common.LoopSoundMessage;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.Collection;

@EventBusSubscriber
public class RegisterCommand {

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(DoomsGreats.MODID).requires(source -> source.hasPermission(Commands.LEVEL_OWNERS)).
                then(Commands.literal("test")
                        .then(Commands.argument("player", EntityArgument.players())
                        .executes(arguments -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(arguments, "player");
                            Level world = arguments.getSource().getUnsidedLevel();
                            return test(players, world);
                        })))
                .executes(arguments -> {
                            Level world = arguments.getSource().getUnsidedLevel();
                            Entity entity = arguments.getSource().getEntity();
                            if (entity == null && world instanceof ServerLevel serverLevel)
                                entity = FakePlayerFactory.getMinecraft(serverLevel);
                            return test((LivingEntity) entity, world);
                        })
                );
    }

    public static int test(Collection<ServerPlayer> players, Level world){
        for (ServerPlayer player : players){
            test(player, world);
        }
        return 0;
    }

    public static int test(LivingEntity entity, Level world){
        if (entity instanceof Player player) {
//            DoomsEffect effect = new DoomsEffect(world, player);
//            effect.setPos(entity.getX(), entity.getY(), entity.getZ());
//            world.addFreshEntity(effect);
            PlayerAnimator.playerAnimation(player, DesireDriver.REVOLVE, true);
            MessageLoader.sendToPlayer(new LoopSoundMessage(ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, DesireDriver.REVOLVE), false, 10, 7, entity.getId()), (ServerPlayer) player);
        }

        if (!world.isClientSide()){
            ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.LEGS);
            Item item = itemStack.getItem();
            if (item instanceof DesireDriver driver){
                ItemHelp.setNbt(itemStack, nbt -> nbt.putBoolean(DesireDriver.REVOLVE, true));
                IGeoItem.playAnimationAndReset(itemStack,"revolveon");
                new WaitToRun(() -> {
                    IGeoItem.playAnimationAndReset(itemStack,DesireDriver.IDLE);
                    ItemHelp.setNbt(itemStack, nbt -> nbt.putBoolean(DesireDriver.REVOLVE, false));
                }, (int)4.2*20);
//                driver.triggerAnim(entity, GeoItem.getOrAssignId(itemStack, (ServerLevel) world), "revolve_controller", DesireDriver.REVOLVE);
            }
        }

        boolean equip = BaseRiderArmorBase.isAllEquip(entity);
        for (EquipmentSlot slot : BaseRiderArmorBase.getSlots()){
            if (equip) BaseRiderArmor.unequip(entity, slot);
            else {
                if (entity instanceof Player player) {
                    ItemStack stack = new ItemStack(ClassRegister.getRegisterObject("buster_qb_9_sword", Item.class).get());
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                }
                BaseRiderArmor.equip(entity, slot, DoomsGreatsArmor.getNewArmor(slot));
            }
        }
        return 0;
    }
}
