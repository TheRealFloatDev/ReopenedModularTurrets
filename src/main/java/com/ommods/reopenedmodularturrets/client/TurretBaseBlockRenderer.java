package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TurretBaseBlockRenderer implements BlockEntityRenderer<TurretBaseBlockEntity> {
    public TurretBaseBlockRenderer(BlockEntityRendererProvider.Context context) {
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
        if (camoState == null) {
            return;
        }
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                camoState,
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );
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
