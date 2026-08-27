package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretBaseModel;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import com.ommods.reopenedmodularturrets.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class TurretBaseItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final TurretBaseModel model;

    public TurretBaseItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new TurretBaseModel(modelSet.bakeLayer(ModModelLayers.TURRET_BASE));
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
        Block block = stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : null;
        ResourceLocation texture = textureForBlock(block);
        if (texture == null) {
            return;
        }
        poseStack.pushPose();
        TurretRenderHelper.prepareItemPose(poseStack, displayContext);
        TurretRenderHelper.prepareTurretPose(poseStack);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }

    private static ResourceLocation textureForBlock(Block block) {
        if (block == ModBlocks.TURRET_BASE_TIER_1.get()) {
            return ModEntityTextures.texture("turret_base_tier_one");
        }
        if (block == ModBlocks.TURRET_BASE_TIER_2.get()) {
            return ModEntityTextures.texture("turret_base_tier_two");
        }
        if (block == ModBlocks.TURRET_BASE_TIER_3.get()) {
            return ModEntityTextures.texture("turret_base_tier_three");
        }
        if (block == ModBlocks.TURRET_BASE_TIER_4.get()) {
            return ModEntityTextures.texture("turret_base_tier_four");
        }
        if (block == ModBlocks.TURRET_BASE_TIER_5.get()) {
            return ModEntityTextures.texture("turret_base_tier_five");
        }
        return null;
    }

    public static TurretBaseItemRenderer create() {
        Minecraft mc = Minecraft.getInstance();
        return new TurretBaseItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
    }
}
