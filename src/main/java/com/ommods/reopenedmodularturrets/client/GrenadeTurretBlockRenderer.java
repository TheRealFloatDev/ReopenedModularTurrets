package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ommods.reopenedmodularturrets.blockentity.DirectedTurretBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import com.ommods.reopenedmodularturrets.core.targeting.TurretAimHelper;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.phys.Vec3;

public class GrenadeTurretBlockRenderer implements BlockEntityRenderer<DirectedTurretBlockEntity, DirectedTurretRenderState> {
    private final SpriteGetter sprites;
    private final GrenadeTurretModel model;

    public GrenadeTurretBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new GrenadeTurretModel(context.bakeLayer(ModModelLayers.GRENADE_TURRET));
    }

    @Override
    public DirectedTurretRenderState createRenderState() {
        return new DirectedTurretRenderState();
    }

    @Override
    public void extractRenderState(
            DirectedTurretBlockEntity blockEntity,
            DirectedTurretRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.rotationX = TurretAimHelper.getRotationXYFromYawPitch(blockEntity.getYaw(), blockEntity.getPitch());
        state.rotationZ = TurretAimHelper.getRotationXZFromYawPitch(blockEntity.getYaw(), blockEntity.getPitch());
    }

    @Override
    public void submit(
            DirectedTurretRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        DirectedTurretModelState modelState = new DirectedTurretModelState(state.rotationX, state.rotationZ);
        model.setupAnim(modelState);
        poseStack.pushPose();
        TurretRenderHelper.prepareTurretPose(poseStack);
        submitNodeCollector.submitModel(
                model,
                modelState,
                poseStack,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                ModEntityTextures.GRENADE_TURRET,
                sprites,
                0,
                state.breakProgress
        );
        poseStack.popPose();
    }
}
