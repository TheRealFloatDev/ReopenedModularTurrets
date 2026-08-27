package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SolarItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final SolarAddonModel model;

    public SolarItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new SolarAddonModel(modelSet.bakeLayer(ModModelLayers.SOLAR_ADDON));
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        RenderType renderType = RenderType.entityCutoutNoCull(ModEntityTextures.SOLAR_ADDON);
        VertexConsumer consumer = buffer.getBuffer(renderType);
        poseStack.pushPose();
        TurretRenderHelper.prepareItemPose(poseStack, displayContext);
        TurretRenderHelper.prepareTurretPose(poseStack);
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }

    public static SolarItemRenderer create() {
        Minecraft mc = Minecraft.getInstance();
        return new SolarItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
    }
}
