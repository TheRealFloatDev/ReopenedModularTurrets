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
        if (yaw < 0.0F) {
            yaw += (float) (2.0D * Math.PI);
        }
        return yaw / (float) Math.PI * 180.0F;
    }

    private static final float TURRET_AIM_Y = 0.35F;

    public static float getAimPitch(Entity target, BlockPos turretPos) {
        Vec3 targetCenter = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
        double deltaX = targetCenter.x - (turretPos.getX() + 0.5D);
        double deltaY = targetCenter.y - (turretPos.getY() + TURRET_AIM_Y);
        double deltaZ = targetCenter.z - (turretPos.getZ() + 0.5D);
        float pitch = (float) Math.atan2(Math.sqrt(deltaZ * deltaZ + deltaX * deltaX), deltaY);
        if (pitch < 0.0F) {
            pitch += (float) (2.0D * Math.PI);
        }
        return pitch / (float) Math.PI * 180.0F;
    }

    public static float getRotationXYFromYawPitch(float yaw, float pitch) {
        return (pitch + 270.0F) / 90.0F * ((float) Math.PI / 2.0F);
    }

    public static float getRotationXZFromYawPitch(float yaw, float pitch) {
        return (yaw - 90.0F) / 90.0F * ((float) Math.PI / 2.0F);
    }

    public static float directionToYaw(Vec3 direction) {
        return (float) Math.toDegrees(Math.atan2(direction.x, direction.z));
    }

    public static float directionToPitch(Vec3 direction) {
        return (float) Math.toDegrees(Math.atan2(direction.y, Math.hypot(direction.x, direction.z)));
    }
}
