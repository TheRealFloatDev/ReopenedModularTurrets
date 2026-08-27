package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

public class TurretBaseBlockRenderer implements BlockEntityRenderer<TurretBaseBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public TurretBaseBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(
            TurretBaseBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        BlockState camoState = blockEntity.getCamoState();
        Level level = blockEntity.getLevel();
        if (camoState == null || level == null || camoState.isAir()) {
            return;
        }

        BlockPos pos = blockEntity.getBlockPos();
        BakedModel model = blockRenderer.getBlockModel(camoState);
        ModelData modelData = ModelData.EMPTY;
        RandomSource random = RandomSource.create(camoState.getSeed(pos));

        ModelBlockRenderer.enableCaching();
        poseStack.pushPose();
        for (var renderType : model.getRenderTypes(camoState, random, modelData)) {
            VertexConsumer consumer = buffer.getBuffer(RenderTypeHelper.getEntityRenderType(renderType, false));
            blockRenderer.getModelRenderer().tesselateBlock(
                    level,
                    model,
                    camoState,
                    pos,
                    poseStack,
                    consumer,
                    false,
                    random,
                    camoState.getSeed(pos),
                    packedOverlay,
                    modelData,
                    renderType
            );
        }
        poseStack.popPose();
        ModelBlockRenderer.clearCache();
    }

    @Override
    public boolean shouldRenderOffScreen(TurretBaseBlockEntity blockEntity) {
        return blockEntity.hasCamo();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
