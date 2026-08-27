package com.ommods.reopenedmodularturrets.client.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import java.util.function.Consumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import org.joml.Vector3fc;

public class GunTurretSpecialRenderer implements NoDataSpecialModelRenderer {
    private final GunTurretModel model;
    private final SpriteGetter sprites;

    public GunTurretSpecialRenderer(GunTurretModel model, SpriteGetter sprites) {
        this.model = model;
        this.sprites = sprites;
    }

    @Override
    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor
    ) {
        DirectedTurretModelState modelState = DirectedTurretModelState.IDLE;
        model.setupAnim(modelState);
        poseStack.pushPose();
        TurretRenderHelper.prepareItemPose(poseStack);
        TurretRenderHelper.prepareTurretPose(poseStack);
        submitNodeCollector.submitModel(
                model,
                modelState,
                poseStack,
                lightCoords,
                overlayCoords,
                -1,
                ModEntityTextures.GUN_TURRET,
                sprites,
                outlineColor,
                null
        );
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        TurretRenderHelper.prepareItemPose(poseStack);
        TurretRenderHelper.prepareTurretPose(poseStack);
        model.setupAnim(DirectedTurretModelState.IDLE);
        model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public GunTurretSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new GunTurretSpecialRenderer(
                    new GunTurretModel(context.entityModelSet().bakeLayer(ModModelLayers.GUN_TURRET)),
                    context.sprites()
            );
        }
    }
}
