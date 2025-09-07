package com.ankrya.doomsgreats.help.other;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SlashLight {

    private static Vector3f[] getExactSwordPosition(AbstractClientPlayer player, Vector3f set) {
        AnimationApplier emote = ((IAnimatedPlayer) player).playerAnimator_getAnimation();
        String hand = player.getMainArm() == HumanoidArm.LEFT ? "left_arm" : "right_arm";
        Vector3f bodyPos = create(emote.get3DTransform(hand, TransformType.POSITION, Vec3f.ZERO));
        Vector3f swordPos = new Vector3f(bodyPos).add(1, 10, -2).add(set);
        Vector3f bodyRot = create(emote.get3DTransform(hand, TransformType.ROTATION, Vec3f.ZERO));
        Vector3f swordRot = calculateTransformedRotation(bodyPos, bodyRot, swordPos);
        return new Vector3f[]{swordPos, swordRot};
    }

    public static Vector3f[] getQB9Position(AbstractClientPlayer player){
        return getExactSwordPosition(player, new Vector3f(0, 2, 10));
    }

    private static Vector3f create(Vec3f vec3f){
        return new Vector3f(vec3f.getX(), vec3f.getY(), vec3f.getZ());
    }

    private static Vector3f calculateTransformedRotation(Vector3f originalPosition, Vector3f originalRotation, Vector3f newPosition) {
        Quaternionf originalRotQuad = eulerToQuaternion(originalRotation);
        Vector3f displacement = new Vector3f(newPosition).sub(originalPosition);
        Vector3f rotatedDisplacement = new Vector3f();
        originalRotQuad.transform(displacement, rotatedDisplacement);
        Vector3f expectedGlobalPos = new Vector3f(originalPosition).add(rotatedDisplacement);
        Vector3f actualToExpected = new Vector3f(expectedGlobalPos).sub(newPosition);
        if (actualToExpected.length() > 1e-6f) {
            Vector3f direction = actualToExpected.normalize();
            Quaternionf correctionQuad = new Quaternionf();
            correctionQuad.rotationTo(new Vector3f(0, 0, 1), direction);
            Quaternionf newRotQuad = new Quaternionf(originalRotQuad).mul(correctionQuad);
            return quaternionToEuler(newRotQuad);
        }

        return new Vector3f(originalRotation);
    }

    /**
     * 将欧拉角转换为四元数
     * @param euler 欧拉角（弧度制，顺序为Z-Y-X）
     * @return 对应的四元数
     */
    private static Quaternionf eulerToQuaternion(Vector3f euler) {
        return new Quaternionf()
                .rotateZ(euler.z)
                .rotateY(euler.y)
                .rotateX(euler.x);
    }

    /**
     * 将四元数转换为欧拉角
     * @param quad 四元数
     * @return 欧拉角（弧度制，顺序为Z-Y-X）
     */
    private static Vector3f quaternionToEuler(Quaternionf quad) {
        // 提取四元数分量
        float x = quad.x;
        float y = quad.y;
        float z = quad.z;
        float w = quad.w;

        // 计算欧拉角
        float roll = (float) Math.atan2(2.0f * (w * x + y * z), 1.0f - 2.0f * (x * x + y * y));
        float pitch = (float) Math.asin(2.0f * (w * y - z * x));
        float yaw = (float) Math.atan2(2.0f * (w * z + x * y), 1.0f - 2.0f * (y * y + z * z));

        return new Vector3f(roll, pitch, yaw);
    }
}
