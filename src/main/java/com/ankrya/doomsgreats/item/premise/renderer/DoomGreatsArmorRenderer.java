package com.ankrya.doomsgreats.item.premise.renderer;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.data.other.PlayerDatePass;
import com.ankrya.doomsgreats.help.GJ;
import com.ankrya.doomsgreats.item.items.armor.DoomsGreatsArmor;
import com.ankrya.doomsgreats.item.premise.renderer.base.BaseRiderArmorRender;
import com.ankrya.doomsgreats.message.MessageLoader;
import com.ankrya.doomsgreats.message.NMessageCreater;
import com.ankrya.doomsgreats.message.ex_message.AllPackt;
import com.ankrya.doomsgreats.message.ex_message.PlayerDataPassToMessage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import software.bernie.geckolib.cache.object.GeoBone;

public class DoomGreatsArmorRenderer extends BaseRiderArmorRender<DoomsGreatsArmor> {
    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(DoomsGreats.MODID, "textures/item/dooms_greats.png");

    public DoomGreatsArmorRenderer() {
        super();
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
        String name = bone.getName();
        if (name.equals("left_eye") || name.equals("right_eye")) {
            VertexConsumer newBuffer = source.getBuffer(RenderType.beaconBeam(LAYER, false));
            super.renderCubesOfBone(poseStack, bone, newBuffer, packedLight, packedOverlay, colour);
        } else super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);
        if (GJ.ToItem.getNbt(this.currentStack).getBoolean(DoomsGreatsArmor.CLOAK_EFFECT)) {
            float RChange = getRChange();
            this.model.getBone("wing_middle").ifPresent(geoBone -> geoBone.setRotX(RChange * -0.16f));
            this.model.getBone("wing_middle_down").ifPresent(geoBone -> geoBone.setRotX(RChange * 0.08f));
            for (int i = 0; i < 12; i++) {
                GeoBone left = getCapeBone(true, i);
                GeoBone right = getCapeBone(false, i);
                if (left != null) left.setRotX(getRRChange(i, RChange));
                if (right != null) right.setRotX(getRRChange(i, RChange));
            }
        }
    }

    private GeoBone getCapeBone(boolean left, int index) {
        return this.model.getBone("wing_film_" + (left ? "left" : "right") + index).orElse(null);
    }

    private float getRRChange(float index, float RChange) {
        int v = (int) (index / 3);
        if (index % 3 == 0) return -0.18f / v * RChange;
        if (index % 3 == 1) return -0.24f / v * RChange;
        if (index % 3 == 2) return -0.16f / v * RChange;
        return 0.08f / v * RChange;
    }

    private float getRChange() {
        Vec3 velocity = getMovementVector();
        float ground_speed = (isMovingForward(getLookVector(), velocity) ? 1 : -1) * Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
        float vertical_speed = (float) velocity.y;
        float RChange = 0;
        if (vertical_speed == 0) vertical_speed = (float) 0.1;
        float CSchange = (float) (((1 - StrictMath.exp(-0.1 * ground_speed)) * 60) - (vertical_speed / Mth.abs(vertical_speed) * (1 - StrictMath.exp(-0.1 * Mth.abs(vertical_speed))) * 30));
        if (RChange - CSchange > 0.3) {
            RChange = (float) (RChange + 0.3);
        } else if (RChange - CSchange < -0.3) {
            RChange = (float) (RChange - 0.3);
        } else {
            RChange = CSchange;
        }
        RChange = CSchange;
        if (RChange > 1.1) {
            RChange = (float) 1.1;
        } else if (RChange < -0.26) {
            RChange = (float) -0.26;
        }
        return RChange;
    }

    private boolean isMovingForward(Vec3 lookVector, Vec3 movementVector) {
        Vec2 lookVectorHorizontal = new Vec2((float) lookVector.x, (float) lookVector.z).normalized();
        Vec2 movementVectorHorizontal = new Vec2((float) movementVector.x, (float) movementVector.z);

        float dotProduct = lookVectorHorizontal.dot(movementVectorHorizontal);

        float threshold = 0.1f;

        return dotProduct > threshold;
    }

    private Vec3 getLookVector() {
        return currentEntity.getLookAngle();
    }

    private Vec3 getMovementVector() {
        return currentEntity.getDeltaMovement();
    }
}