package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
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

    public TurretHeadBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.gunModel = new GunTurretModel(context.bakeLayer(ModModelLayers.GUN_TURRET));
        this.grenadeModel = new GrenadeTurretModel(context.bakeLayer(ModModelLayers.GRENADE_TURRET));
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
        poseStack.popPose();
    }
}
