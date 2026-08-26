package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ommods.reopenedmodularturrets.blockentity.GunTurretBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.Unit;

public class GunTurretBlockRenderer implements BlockEntityRenderer<GunTurretBlockEntity, BlockEntityRenderState> {
    private final SpriteGetter sprites;
    private final GunTurretModel model;

    public GunTurretBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new GunTurretModel(context.bakeLayer(ModModelLayers.GUN_TURRET));
    }

    @Override
    public BlockEntityRenderState createRenderState() {
        return new BlockEntityRenderState();
    }

    @Override
    public void submit(
            BlockEntityRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        poseStack.pushPose();
        TurretRenderHelper.prepareTurretPose(poseStack);
        submitNodeCollector.submitModel(
                model,
                Unit.INSTANCE,
                poseStack,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                ModEntityTextures.GUN_TURRET,
                sprites,
                0,
                state.breakProgress
        );
        poseStack.popPose();
    }
}
