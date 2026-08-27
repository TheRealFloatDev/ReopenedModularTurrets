package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.ProjectileModel;
import com.ommods.reopenedmodularturrets.entity.GrenadeProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GrenadeProjectileRenderer extends EntityRenderer<GrenadeProjectileEntity> {
    private final ProjectileModel model;

    public GrenadeProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ProjectileModel(context.bakeLayer(ModModelLayers.PROJECTILE));
    }

    @Override
    public void render(
            GrenadeProjectileEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getXRot()));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        var consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GrenadeProjectileEntity entity) {
        return ModEntityTextures.texture(entity.getProjectileTexture());
    }
}
