package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.blockentity.LootDeleterAddonBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.LootDeleterAddonModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class LootDeleterAddonBlockRenderer implements BlockEntityRenderer<LootDeleterAddonBlockEntity> {
    private final LootDeleterAddonModel model;

    public LootDeleterAddonBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LootDeleterAddonModel(context.bakeLayer(ModModelLayers.LOOT_DELETER_ADDON));
    }

    @Override
    public void render(
            LootDeleterAddonBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        poseStack.pushPose();
        TurretRenderHelper.prepareBlockEntityPose(poseStack);
        RenderType sideType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("base_addon_loot_deleter_side"));
        VertexConsumer sideConsumer = buffer.getBuffer(sideType);
        model.renderBody(poseStack, sideConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        RenderType topType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("base_addon_loot_deleter_top"));
        VertexConsumer topConsumer = buffer.getBuffer(topType);
        model.renderTop(poseStack, topConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }
}
