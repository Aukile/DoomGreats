package com.ankrya.doomsgreats.entity.renderer;

import com.ankrya.doomsgreats.entity.SpecialEffect;
import com.ankrya.doomsgreats.entity.model.SpecialEffectModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.RenderUtil;

import java.util.Collections;

public class SpecialEffectRenderer<T extends SpecialEffect> extends GeoEntityRenderer<T> {

    protected final GeoModel<T> modelProvider;
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();
    protected T animatable;

    public SpecialEffectRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.modelProvider = model;
    }

    public SpecialEffectRenderer(EntityRendererProvider.Context renderManager){
        this(renderManager, new SpecialEffectModel<>());
    }

    @Override
    public void render(@NotNull T animatable, float yaw, float partialTick, PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight) {
        BakedGeoModel model = this.modelProvider.getBakedModel(this.modelProvider.getModelResource(animatable, this));
        this.dispatchedMat = poseStack.last().pose();

        poseStack.pushPose();
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(180f - lerpBodyRot));

        AnimationState<T> predicate = new AnimationState<>(animatable, 0, 0, partialTick, false);

        ResourceLocation textureResource = this.modelProvider.getTextureResource(animatable, this);
        modelProvider.setCustomAnimations(animatable, getInstanceId(animatable), predicate);
        RenderSystem.setShaderTexture(0, textureResource);
        poseStack.translate(0, -0.01f, 0);
        Color renderColor = getRenderColor(animatable, partialTick, packedLight);
        RenderType renderType = getRenderType(animatable, textureResource, bufferSource, partialTick);
        if (renderType != null){
            VertexConsumer buffer = bufferSource.getBuffer(renderType);
            actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, false, partialTick, packedLight, OverlayTexture.NO_OVERLAY, renderColor.argbInt());
        }
        poseStack.popPose();
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = poseStack.last().pose();
            Matrix4f localMatrix = RenderUtil.invertAndMultiplyMatrices(poseState, this.dispatchedMat);

            bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.translate(getRenderOffset(this.animatable, 1).toVector3f());
            bone.setLocalSpaceMatrix(localMatrix);

            Matrix4f worldState = localMatrix;

            worldState.translate(this.animatable.position().toVector3f());
            bone.setWorldSpaceMatrix(worldState);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        this.renderEarlyMat = poseStack.last().pose();
        this.animatable = animatable;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    public static int getPackedOverlay(Entity entity, float uIn) {
        return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(false));
    }

    @Override
    public long getInstanceId(T animatable) {
        return animatable.getUUID().hashCode();
    }
}
