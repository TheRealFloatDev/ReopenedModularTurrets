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
import com.ommods.reopenedmodularturrets.core.targeting.TurretAimHelper;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
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
        TurretKind kind = blockEntity.getKind();
        float rotationX = 0.0F;
        float rotationZ = 0.0F;
        if (kind.isDirected()) {
            rotationX = TurretAimHelper.getRotationXYFromYawPitch(blockEntity.getYaw(), blockEntity.getPitch());
            rotationZ = TurretAimHelper.getRotationXZFromYawPitch(blockEntity.getYaw(), blockEntity.getPitch());
        }
        DirectedTurretModelState modelState = new DirectedTurretModelState(rotationX, rotationZ);
        RenderType renderType = RenderType.entityCutoutNoCull(ModEntityTextures.forTurret(kind));
        VertexConsumer consumer = buffer.getBuffer(renderType);

        poseStack.pushPose();
        TurretRenderHelper.prepareTurretPose(poseStack);
        if (kind.usesGrenadeModel()) {
            grenadeModel.setupAnim(modelState);
            grenadeModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        } else {
            gunModel.setupAnim(modelState);
            gunModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
        poseStack.popPose();
    }
}
