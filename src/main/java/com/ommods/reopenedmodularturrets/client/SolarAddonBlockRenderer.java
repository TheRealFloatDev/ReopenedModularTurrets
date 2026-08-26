package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ommods.reopenedmodularturrets.blockentity.SolarAddonBlockEntity;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.Unit;

public class SolarAddonBlockRenderer implements BlockEntityRenderer<SolarAddonBlockEntity, BlockEntityRenderState> {
    private final SpriteGetter sprites;
    private final SolarAddonModel model;

    public SolarAddonBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new SolarAddonModel(context.bakeLayer(ModModelLayers.SOLAR_ADDON));
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
                ModEntityTextures.SOLAR_ADDON,
                sprites,
                0,
                state.breakProgress
        );
        poseStack.popPose();
    }
}
