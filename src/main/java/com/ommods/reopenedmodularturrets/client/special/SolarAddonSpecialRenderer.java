package com.ommods.reopenedmodularturrets.client.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import java.util.function.Consumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;

public class SolarAddonSpecialRenderer implements NoDataSpecialModelRenderer {
    private final SolarAddonModel model;
    private final SpriteGetter sprites;

    public SolarAddonSpecialRenderer(SolarAddonModel model, SpriteGetter sprites) {
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
        poseStack.pushPose();
        TurretRenderHelper.prepareItemPose(poseStack);
        TurretRenderHelper.prepareTurretPose(poseStack);
        submitNodeCollector.submitModel(
                model,
                Unit.INSTANCE,
                poseStack,
                lightCoords,
                overlayCoords,
                -1,
                ModEntityTextures.SOLAR_ADDON,
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
        model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SolarAddonSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new SolarAddonSpecialRenderer(
                    new SolarAddonModel(context.entityModelSet().bakeLayer(ModModelLayers.SOLAR_ADDON)),
                    context.sprites()
            );
        }
    }
}
