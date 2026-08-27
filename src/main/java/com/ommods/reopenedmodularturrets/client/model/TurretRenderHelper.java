package com.ommods.reopenedmodularturrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

public final class TurretRenderHelper {
    private TurretRenderHelper() {}

    public static void prepareBlockEntityPose(PoseStack poseStack) {
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, -1.0F, 1.0F);
    }

    public static void prepareTurretItemPose(PoseStack poseStack, ItemDisplayContext context) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        switch (context) {
            case GUI -> {
                poseStack.translate(-0.14F, 0.10F, 0.0F);
                poseStack.scale(0.55F, 0.55F, 0.55F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
                poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(25.0F)));
            }
            case GROUND -> {
                poseStack.scale(0.42F, 0.42F, 0.42F);
                poseStack.translate(0.0F, -0.32F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
            case FIXED -> {
                poseStack.scale(0.52F, 0.52F, 0.52F);
                poseStack.translate(0.0F, -0.22F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
            default -> {
                poseStack.scale(0.48F, 0.48F, 0.48F);
                poseStack.translate(0.0F, -0.22F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
        }
    }

    public static void prepareBaseItemPose(PoseStack poseStack, ItemDisplayContext context) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        switch (context) {
            case GUI -> {
                poseStack.translate(-0.12F, 0.08F, 0.0F);
                poseStack.scale(0.70F, 0.70F, 0.70F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
                poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(25.0F)));
            }
            default -> {
                poseStack.scale(0.55F, 0.55F, 0.55F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
        }
    }

    public static void prepareLeverItemPose(PoseStack poseStack, ItemDisplayContext context) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        if (context == ItemDisplayContext.GUI) {
            poseStack.translate(-0.10F, 0.06F, 0.0F);
            poseStack.scale(0.80F, 0.80F, 0.80F);
            poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(20.0F)));
        } else {
            poseStack.scale(0.6F, 0.6F, 0.6F);
        }
    }

    /** @deprecated use {@link #prepareBlockEntityPose(PoseStack)} */
    @Deprecated
    public static void prepareTurretPose(PoseStack poseStack) {
        prepareBlockEntityPose(poseStack);
    }

    /** @deprecated use {@link #prepareTurretItemPose(PoseStack, ItemDisplayContext)} */
    @Deprecated
    public static void prepareItemPose(PoseStack poseStack, ItemDisplayContext context) {
        prepareTurretItemPose(poseStack, context);
    }
}
