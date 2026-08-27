package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.RedstoneReactorAddonModel;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
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
    private final GunTurretModel gunModel;
    private final GrenadeTurretModel grenadeModel;
    private final SolarAddonModel solarModel;
    private final RedstoneReactorAddonModel reactorModel;

    public TurretHeadBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.gunModel = new GunTurretModel(context.bakeLayer(ModModelLayers.GUN_TURRET));
        this.grenadeModel = new GrenadeTurretModel(context.bakeLayer(ModModelLayers.GRENADE_TURRET));
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
        if (blockEntity.getBase() != null && blockEntity.getBase().getAddonState().concealer()) {
            return;
        }
        TurretKind kind = blockEntity.getKind();
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
        if (kind.usesGrenadeModel()) {
            grenadeModel.setupAnim(modelState);
            grenadeModel.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        } else {
            gunModel.setupAnim(modelState);
            gunModel.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }
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
