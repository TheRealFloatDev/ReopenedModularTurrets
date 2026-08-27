package com.ommods.reopenedmodularturrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class TurretRenderHelper {
    private static final Vector3f ITEM_ROTATION_AXIS = new Vector3f(2.5F, -4.5F, -1.0F).normalize();

    private TurretRenderHelper() {}

    public static void prepareTurretPose(PoseStack poseStack) {
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, -1.0F, -1.0F);
    }

    public static void prepareItemPose(PoseStack poseStack, ItemDisplayContext context) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        switch (context) {
            case GUI -> {
                poseStack.scale(0.62F, 0.62F, 0.62F);
                poseStack.translate(0.0F, -0.15F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
                poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(20.0F)));
            }
            case GROUND -> {
                poseStack.scale(0.45F, 0.45F, 0.45F);
                poseStack.translate(0.0F, -0.35F, 0.0F);
            }
            case FIXED -> {
                poseStack.scale(0.55F, 0.55F, 0.55F);
                poseStack.translate(0.0F, -0.25F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(45.0F)));
            }
            default -> {
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.translate(0.0F, -0.25F, 0.0F);
                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(45.0F), ITEM_ROTATION_AXIS));
            }
        }
    }
}
