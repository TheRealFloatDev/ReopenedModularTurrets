package com.ommods.reopenedmodularturrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

public final class TurretRenderHelper {
    private TurretRenderHelper() {}

    /** Matches OMT {@code AbstractTurretRenderer} world pose: translate + scale(1, -1, -1). */
    public static void prepareBlockEntityPose(PoseStack poseStack) {
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, -1.0F, -1.0F);
    }

    /**
     * Matches OMT inventory pose when {@code turretHead == null}.
     * Minecraft 1.21 applies {@code translate(-0.5, -0.5, -0.5)} before BEWLR, so we offset accordingly.
     */
    private static void prepareGuiTurretItemPose(PoseStack poseStack) {
        // MC pre-applies translate(-0.5, -0.5, -0.5); offset tuned to center icons in 16x16 slots.
        poseStack.translate(0.70F, 1.50F, 0.75F);
        poseStack.scale(0.7F, -0.7F, -0.7F);
        poseStack.translate(0.0F, 0.4F, 0.5F);
        poseStack.mulPose(new Quaternionf().rotateAxis((float) Math.toRadians(45.0F), 2.5F, -4.5F, -1.0F));
    }

    public static void prepareTurretItemPose(PoseStack poseStack, ItemDisplayContext context) {
        switch (context) {
            case GUI -> prepareGuiTurretItemPose(poseStack);
            case GROUND -> {
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.scale(0.42F, 0.42F, 0.42F);
                poseStack.translate(0.0F, -0.32F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
            case FIXED -> {
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.scale(0.52F, 0.52F, 0.52F);
                poseStack.translate(0.0F, -0.22F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
            default -> {
                poseStack.translate(0.5F, 0.5F, 0.5F);
                poseStack.scale(0.48F, 0.48F, 0.48F);
                poseStack.translate(0.0F, -0.22F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
        }
    }

    public static void prepareBaseItemPose(PoseStack poseStack, ItemDisplayContext context) {
        if (context == ItemDisplayContext.GUI) {
            prepareGuiTurretItemPose(poseStack);
            return;
        }
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(0.55F, 0.55F, 0.55F);
        poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
    }

    public static void prepareLeverItemPose(PoseStack poseStack, ItemDisplayContext context) {
        if (context == ItemDisplayContext.GUI) {
            prepareGuiTurretItemPose(poseStack);
        } else {
            poseStack.translate(0.5F, 0.5F, 0.5F);
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
