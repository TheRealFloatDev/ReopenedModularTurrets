package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.ProjectileModel;
import com.ommods.reopenedmodularturrets.core.targeting.TurretAimHelper;
import com.ommods.reopenedmodularturrets.entity.LaserBeamEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class LaserBeamRenderer extends EntityRenderer<LaserBeamEntity> {
    private static final int LASER_COLOR = 0xFFFF2200;
    private static final int LASER_CORE_COLOR = 0xFFFFAA88;
    private static final float BEAM_THICKNESS = 0.48F;
    private static final float CORE_THICKNESS = 0.26F;
    /** Projectile cube spans 4/16 block along Z at unit scale. */
    private static final float BEAM_MODEL_DEPTH = 0.25F;
    private final ProjectileModel model;

    public LaserBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ProjectileModel(context.bakeLayer(ModModelLayers.PROJECTILE));
    }

    @Override
    public void render(
            LaserBeamEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        Vec3 start = entity.position();
        Vec3 end = entity.getEnd();
        Vec3 delta = end.subtract(start);
        double fullLength = delta.length();
        if (fullLength < 0.01D) {
            return;
        }

        float progress = entity.getBeamProgress(partialTick);
        float traveledLength = (float) fullLength * progress;
        if (traveledLength < 0.02F) {
            return;
        }

        float beamDepthScale = traveledLength / BEAM_MODEL_DEPTH;
        Vec3 direction = delta.scale(1.0D / fullLength);
        float yaw = TurretAimHelper.directionToYaw(direction);
        float pitch = TurretAimHelper.directionToPitch(direction);
        var renderType = RenderType.entityTranslucentEmissive(getTextureLocation(entity));

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        poseStack.translate(0.0F, 0.0F, traveledLength * 0.5F);
        poseStack.scale(BEAM_THICKNESS, BEAM_THICKNESS, beamDepthScale);
        model.renderToBuffer(
                poseStack,
                buffer.getBuffer(renderType),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                LASER_COLOR
        );
        poseStack.popPose();

        float tipDepthScale = Math.min(0.4F, traveledLength) / BEAM_MODEL_DEPTH;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        poseStack.translate(0.0F, 0.0F, traveledLength - (tipDepthScale * BEAM_MODEL_DEPTH * 0.5F));
        poseStack.scale(CORE_THICKNESS, CORE_THICKNESS, tipDepthScale);
        model.renderToBuffer(
                poseStack,
                buffer.getBuffer(renderType),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                LASER_CORE_COLOR
        );
        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LaserBeamEntity entity) {
        return ModEntityTextures.texture("ammo_bullet");
    }
}
