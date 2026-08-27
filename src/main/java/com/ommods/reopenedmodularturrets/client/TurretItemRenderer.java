package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.client.model.AnimatedTurretModel;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.TurretModels;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TurretItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final TurretModels turretModels;

    public TurretItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.turretModels = new TurretModels(modelSet::bakeLayer);
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
        TurretKind kind = TurretKind.fromItem(stack.getItem());
        if (kind == null) {
            return;
        }
        AnimatedTurretModel model = turretModels.get(kind);
        if (model == null) {
            return;
        }
        DirectedTurretModelState modelState = DirectedTurretModelState.IDLE;
        RenderType renderType = RenderType.entityCutoutNoCull(ModEntityTextures.forTurret(kind));
        VertexConsumer consumer = buffer.getBuffer(renderType);
        poseStack.pushPose();
        TurretRenderHelper.prepareTurretItemPose(poseStack, displayContext);
        model.setupAnim(modelState);
        model.asPartModel().renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
    }

    public static TurretItemRenderer create() {
        Minecraft mc = Minecraft.getInstance();
        return new TurretItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
    }
}
