package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.AnimatedTurretModel;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.RedstoneReactorAddonModel;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.client.model.TurretModels;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import com.ommods.reopenedmodularturrets.core.addons.AddonState;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TurretHeadBlockRenderer implements BlockEntityRenderer<TurretHeadBlockEntity> {
    private final TurretModels turretModels;
    private final SolarAddonModel solarModel;
    private final RedstoneReactorAddonModel reactorModel;

    public TurretHeadBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.turretModels = new TurretModels(context::bakeLayer);
        this.solarModel = new SolarAddonModel(context.bakeLayer(ModModelLayers.SOLAR_ADDON));
        this.reactorModel = new RedstoneReactorAddonModel(context.bakeLayer(ModModelLayers.REDSTONE_REACTOR_ADDON));
    }

    @Override
    public void render(
            TurretHeadBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        if (blockEntity.isConcealed()) {
            return;
        }
        TurretKind kind = blockEntity.getKind();
        AnimatedTurretModel model = turretModels.get(kind);
        if (model == null) {
            return;
        }
        DirectedTurretModelState modelState = DirectedTurretModelState.IDLE;
        if (kind.isDirected()) {
            modelState = DirectedTurretModelState.aimed(
                    blockEntity.getYaw(),
                    blockEntity.getPitch(),
                    blockEntity.getBaseDirection()
            );
        }
        RenderType renderType = RenderType.entityCutoutNoCull(ModEntityTextures.forTurret(kind));
        VertexConsumer consumer = buffer.getBuffer(renderType);
        int light = Math.max(packedLight, LightTexture.FULL_BRIGHT);

        poseStack.pushPose();
        TurretRenderHelper.prepareBlockEntityPose(poseStack);
        model.setupAnim(modelState);
        model.asPartModel().renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        renderMountedAddons(blockEntity, modelState, poseStack, buffer, light);
        poseStack.popPose();
    }

    private void renderMountedAddons(
            TurretHeadBlockEntity blockEntity,
            DirectedTurretModelState modelState,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light
    ) {
        if (blockEntity.getBase() == null) {
            return;
        }
        AddonState addons = blockEntity.getBase().getAddonState();
        float rotationX = modelState.rotationX();
        float rotationZ = modelState.rotationZ();
        if (addons.solar()) {
            solarModel.setRotationForTarget(rotationX, rotationZ);
            solarModel.renderToBuffer(
                    poseStack,
                    buffer.getBuffer(RenderType.entityCutoutNoCull(ModEntityTextures.SOLAR_ADDON)),
                    light,
                    OverlayTexture.NO_OVERLAY,
                    0xFFFFFFFF
            );
        }
        if (addons.redstoneReactor()) {
            reactorModel.setRotationForTarget(rotationX, rotationZ);
            reactorModel.renderToBuffer(
                    poseStack,
                    buffer.getBuffer(RenderType.entityCutoutNoCull(ModEntityTextures.texture("addon_redstone_reactor"))),
                    light,
                    OverlayTexture.NO_OVERLAY,
                    0xFFFFFFFF
            );
        }
    }
}
