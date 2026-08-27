package com.ommods.reopenedmodularturrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class TurretRenderHelper {
    private static final Vector3f ITEM_ROTATION_AXIS = new Vector3f(2.5F, -4.5F, -1.0F).normalize();

    private TurretRenderHelper() {}

    public static void prepareTurretPose(PoseStack poseStack) {
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, -1.0F, -1.0F);
    }

    public static void prepareItemPose(PoseStack poseStack) {
        poseStack.scale(0.7F, -0.7F, -0.7F);
        poseStack.translate(0.0F, 0.4F, 0.5F);
        poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(45.0F), ITEM_ROTATION_AXIS));
    }
}
