package com.ommods.reopenedmodularturrets.core.targeting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class TurretAimHelper {
    private TurretAimHelper() {}

    public static float getAimYaw(Entity target, BlockPos turretPos) {
        Vec3 targetPos = target.position();
        double deltaX = targetPos.x - turretPos.getX();
        double deltaZ = targetPos.z - turretPos.getZ();
        float yaw = (float) Math.atan2(deltaZ, deltaX);
        if (yaw < 0) {
            yaw += (float) (2 * Math.PI);
        }
        return yaw / (float) Math.PI * 180F;
    }

    public static float getAimPitch(Entity target, BlockPos turretPos) {
        Vec3 targetPos = target.position();
        double deltaX = Math.floor(targetPos.x) + 0.5F - (turretPos.getX() + 0.5F);
        double deltaY = Math.floor(targetPos.y) + 0.5F - (turretPos.getY() - 0.5F);
        double deltaZ = Math.floor(targetPos.z) + 0.5F - (turretPos.getZ() + 0.5F);
        float pitch = (float) Math.atan2(Math.sqrt(deltaZ * deltaZ + deltaX * deltaX), deltaY);
        if (pitch < 0) {
            pitch += (float) (2 * Math.PI);
        }
        return pitch / (float) Math.PI * 180F;
    }

    public static float getRotationXYFromYawPitch(float yaw, float pitch) {
        return (float) ((pitch + 270F) / 90F * (Math.PI / 2F));
    }

    public static float getRotationXZFromYawPitch(float yaw, float pitch) {
        return (float) ((yaw - 90F) / 90F * (Math.PI / 2F));
    }
}
