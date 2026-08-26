package com.ommods.reopenedmodularturrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;

public final class TurretRenderHelper {
    private TurretRenderHelper() {}

    public static void prepareTurretPose(PoseStack poseStack) {
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, -1.0F, -1.0F);
    }
}
