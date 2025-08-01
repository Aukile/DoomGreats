package com.ankrya.doomsgreats.init;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.item.DoomGreatsArmor;
import com.ankrya.doomsgreats.item.base.BaseRiderArmor;
import com.ankrya.doomsgreats.item.base.BaseRiderArmorBase;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

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
                            return test(players);
                        }))).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    return test((LivingEntity) entity);
                }));
    }

    public static int test(Collection<ServerPlayer> players){
        for (ServerPlayer player : players){
            test(player);
        }
        return 0;
    }

    public static int test(LivingEntity entity){
        for (EquipmentSlot slot : BaseRiderArmorBase.getSlots()){
            if (BaseRiderArmor.allArmorEquip(entity))
                BaseRiderArmor.unequip(entity, slot);
            else BaseRiderArmor.equip(entity, slot, DoomGreatsArmor.getNewArmor(slot));
        }
        return 0;
    }
}
