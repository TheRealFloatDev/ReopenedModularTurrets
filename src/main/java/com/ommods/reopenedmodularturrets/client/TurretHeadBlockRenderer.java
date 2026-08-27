package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import com.ommods.reopenedmodularturrets.core.targeting.TurretAimHelper;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.Vec3;

public class TurretHeadBlockRenderer implements BlockEntityRenderer<TurretHeadBlockEntity, DirectedTurretRenderState> {
    private final SpriteGetter sprites;
    private final GunTurretModel gunModel;
    private final GrenadeTurretModel grenadeModel;

    public TurretHeadBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.gunModel = new GunTurretModel(context.bakeLayer(ModModelLayers.GUN_TURRET));
        this.grenadeModel = new GrenadeTurretModel(context.bakeLayer(ModModelLayers.GRENADE_TURRET));
    }

    @Override
    public DirectedTurretRenderState createRenderState() {
        return new DirectedTurretRenderState();
    }

    @Override
    public void extractRenderState(
            TurretHeadBlockEntity blockEntity,
            DirectedTurretRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        if (blockEntity.getKind().isDirected()) {
            state.rotationX = TurretAimHelper.getRotationXYFromYawPitch(blockEntity.getYaw(), blockEntity.getPitch());
            state.rotationZ = TurretAimHelper.getRotationXZFromYawPitch(blockEntity.getYaw(), blockEntity.getPitch());
        } else {
            state.rotationX = 0.0F;
            state.rotationZ = 0.0F;
        }
        state.turretKind = blockEntity.getKind();
    }

    @Override
    public void submit(
            DirectedTurretRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        TurretKind kind = state.turretKind != null ? state.turretKind : TurretKind.GUN;
        DirectedTurretModelState modelState = new DirectedTurretModelState(state.rotationX, state.rotationZ);
        poseStack.pushPose();
        TurretRenderHelper.prepareTurretPose(poseStack);
        if (kind.usesGrenadeModel()) {
            grenadeModel.setupAnim(modelState);
            submitNodeCollector.submitModel(
                    grenadeModel,
                    modelState,
                    poseStack,
                    state.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    -1,
                    ModEntityTextures.forTurret(kind),
                    sprites,
                    0,
                    state.breakProgress
            );
        } else {
            gunModel.setupAnim(modelState);
            submitNodeCollector.submitModel(
                    gunModel,
                    modelState,
                    poseStack,
                    state.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    -1,
                    ModEntityTextures.forTurret(kind),
                    sprites,
                    0,
                    state.breakProgress
            );
        }
        poseStack.popPose();
    }
}
