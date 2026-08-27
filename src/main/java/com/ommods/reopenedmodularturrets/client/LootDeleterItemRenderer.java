package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.client.model.LootDeleterAddonModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
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

public class LootDeleterItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final LootDeleterAddonModel model;

    public LootDeleterItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new LootDeleterAddonModel(modelSet.bakeLayer(ModModelLayers.LOOT_DELETER_ADDON));
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
        poseStack.pushPose();
        TurretRenderHelper.prepareTurretItemPose(poseStack, displayContext);
        RenderType sideType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("base_addon_loot_deleter_side"));
        model.renderBody(poseStack, buffer.getBuffer(sideType), packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        RenderType topType = RenderType.entityCutoutNoCull(ModEntityTextures.texture("base_addon_loot_deleter_top"));
        model.renderTop(poseStack, buffer.getBuffer(topType), packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }

    public static LootDeleterItemRenderer create() {
        Minecraft mc = Minecraft.getInstance();
        return new LootDeleterItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
    }
}
