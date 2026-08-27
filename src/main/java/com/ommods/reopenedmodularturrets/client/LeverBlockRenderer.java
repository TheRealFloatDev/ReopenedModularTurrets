package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ommods.reopenedmodularturrets.block.LeverBlock;
import com.ommods.reopenedmodularturrets.blockentity.LeverBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.LeverBlockModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class LeverBlockRenderer implements BlockEntityRenderer<LeverBlockEntity> {
    private final LeverBlockModel model;

    public LeverBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LeverBlockModel(context.bakeLayer(ModModelLayers.LEVER_BLOCK));
    }

    @Override
    public void render(
            LeverBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        int rotation = blockEntity.getBlockState().getValue(LeverBlock.ROTATION);
        RenderType renderType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("lever_block"));
        VertexConsumer consumer = buffer.getBuffer(renderType);
        poseStack.pushPose();
        TurretRenderHelper.prepareBlockEntityPose(poseStack);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * -22.5F));
        model.setupAnim(blockEntity.getCrankRotation(partialTick));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }
}
