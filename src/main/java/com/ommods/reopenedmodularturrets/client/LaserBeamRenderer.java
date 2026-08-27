package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.ProjectileModel;
import com.ommods.reopenedmodularturrets.entity.LaserBeamEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class LaserBeamRenderer extends EntityRenderer<LaserBeamEntity> {
    private static final int LASER_COLOR = 0xFF00FFFF;
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
        double length = delta.length();
        if (length < 0.01D) {
            return;
        }
        float yaw = (float) Math.toDegrees(Math.atan2(delta.z, delta.x));
        float pitch = (float) Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.scale(0.2F, 0.2F, (float) length * 2.0F);
        var consumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, LASER_COLOR);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LaserBeamEntity entity) {
        return ModEntityTextures.texture("ammo_bullet");
    }
}
