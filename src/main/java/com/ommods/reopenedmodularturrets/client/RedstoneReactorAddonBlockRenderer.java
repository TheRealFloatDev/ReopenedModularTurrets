package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.blockentity.RedstoneReactorAddonBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.RedstoneReactorAddonModel;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class RedstoneReactorAddonBlockRenderer implements BlockEntityRenderer<RedstoneReactorAddonBlockEntity> {
    private final RedstoneReactorAddonModel model;

    public RedstoneReactorAddonBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new RedstoneReactorAddonModel(context.bakeLayer(ModModelLayers.REDSTONE_REACTOR_ADDON));
    }

    @Override
    public void render(
            RedstoneReactorAddonBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        RenderType renderType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("addon_redstone_reactor"));
        VertexConsumer consumer = buffer.getBuffer(renderType);
        poseStack.pushPose();
        TurretRenderHelper.prepareBlockEntityPose(poseStack);
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }
}
