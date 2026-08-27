package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ommods.reopenedmodularturrets.block.BaseAttachmentBlock;
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
import net.minecraft.core.Direction;

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
        Direction facing = blockEntity.getBlockState().getValue(BaseAttachmentBlock.FACING);
        poseStack.pushPose();
        TurretRenderHelper.prepareBlockEntityPose(poseStack);
        poseStack.translate(facing.getStepX() * 0.325F, facing.getStepY() * 0.325F, facing.getStepZ() * 0.325F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        if (facing.getAxis().isVertical()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(facing == Direction.UP ? -90.0F : 90.0F));
        }
        RenderType sideType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("base_addon_loot_deleter_side"));
        model.renderBody(poseStack, buffer.getBuffer(sideType), packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        RenderType topType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("base_addon_loot_deleter_top"));
        model.renderTop(poseStack, buffer.getBuffer(topType), packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }
}
