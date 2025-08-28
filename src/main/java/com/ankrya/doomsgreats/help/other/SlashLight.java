package com.ankrya.doomsgreats.help.other;

import com.ankrya.doomsgreats.data.ModVariable;
import com.ankrya.doomsgreats.data.Variables;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SlashLight {
    private static Vec3 getExactSwordPosition(AbstractClientPlayer player) {
        PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance()
                .getEntityRenderDispatcher().getRenderer(player);
        PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
        PoseStack poseStack = new PoseStack();
        return calculateWeaponPosition(player, model, poseStack);
    }

    private static Vec3 calculateWeaponPosition(Player player, PlayerModel<AbstractClientPlayer> model, PoseStack poseStack) {
        poseStack.pushPose();

        try {
            model.setupAnim((AbstractClientPlayer) player, 0, 0, player.tickCount, 0, 0);

            HumanoidArm arm = player.getMainArm();
            boolean rightHanded = arm == HumanoidArm.RIGHT;
            ModelPart armPart = rightHanded ? model.rightArm : model.leftArm;
            ModelPart handPart = rightHanded ? model.rightSleeve : model.leftSleeve;

            Vec3 handPos = getModelPartWorldPosition(armPart, handPart, poseStack, player);
            Vec3 lookVec = player.getLookAngle();
            float armXRot = armPart.xRot;
            float armYRot = armPart.yRot;
            float armZRot = armPart.zRot;
            Vec3 weaponDir = applyRotation(lookVec, armXRot, armYRot, armZRot);

            double weaponLength = 0.7;

            return handPos.add(weaponDir.scale(weaponLength));
        } finally {
            poseStack.popPose();
        }
    }

    private static Vec3 getModelPartWorldPosition(ModelPart armPart, ModelPart handPart, PoseStack poseStack, Player player) {
        float armX = armPart.x;
        float armY = armPart.y;
        float armZ = armPart.z;

        float handX = handPart.x;
        float handY = handPart.y;
        float handZ = handPart.z;

        Vec3 relativePos = new Vec3(armX + handX, armY + handY, armZ + handZ);

        relativePos = applyRotation(relativePos, armPart.xRot, armPart.yRot, armPart.zRot);
        relativePos = applyRotation(relativePos, handPart.xRot, handPart.yRot, handPart.zRot);

        return player.position().add(relativePos);
    }

    private static Vec3 applyRotation(Vec3 vector, float xRot, float yRot, float zRot) {
        Quaternionf rotation = new Quaternionf();
        rotation.rotationZYX(zRot, yRot, xRot);

        Vector3f vec = new Vector3f((float)vector.x, (float)vector.y, (float)vector.z);
        vec.rotate(rotation);

        return new Vec3(vec.x(), vec.y(), vec.z());
    }

    public static Vec3 getSwordPosition(AbstractClientPlayer player) {
        Vec3 swordPos = getExactSwordPosition(player);
        Variables.setVariable(player, ModVariable.WEAPON_POSITIONS, swordPos);
        return Variables.getVariable(player, ModVariable.WEAPON_POSITIONS);
    }
}
