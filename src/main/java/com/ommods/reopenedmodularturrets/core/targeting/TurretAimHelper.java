package com.ommods.reopenedmodularturrets.core.targeting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class TurretAimHelper {
    private TurretAimHelper() {}

    public static float getAimYaw(Entity target, BlockPos turretPos) {
        Vec3 targetPos = target.position();
        double deltaX = targetPos.x - (turretPos.getX() + 0.5D);
        double deltaZ = targetPos.z - (turretPos.getZ() + 0.5D);
        float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX));
        return normalizeYaw(yaw);
    }

    public static float getAimPitch(Entity target, BlockPos turretPos) {
        Vec3 targetPos = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
        double deltaX = targetPos.x - (turretPos.getX() + 0.5D);
        double deltaY = targetPos.y - (turretPos.getY() + 0.5D);
        double deltaZ = targetPos.z - (turretPos.getZ() + 0.5D);
        double horizontal = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        return (float) Math.toDegrees(Math.atan2(-deltaY, horizontal));
    }

    public static float getRotationXYFromYawPitch(float yaw, float pitch) {
        return (float) Math.toRadians(pitch);
    }

    public static float getRotationXZFromYawPitch(float yaw, float pitch) {
        return (float) Math.toRadians(normalizeYaw(yaw));
    }

    private static float normalizeYaw(float yaw) {
        while (yaw <= -180.0F) {
            yaw += 360.0F;
        }
        while (yaw > 180.0F) {
            yaw -= 360.0F;
        }
        return yaw;
    }
}
