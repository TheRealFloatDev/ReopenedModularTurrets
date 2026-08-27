package com.ommods.reopenedmodularturrets.client.model;

import com.ommods.reopenedmodularturrets.core.targeting.TurretAimHelper;
import net.minecraft.core.Direction;

public record DirectedTurretModelState(
        float rotationX,
        float rotationZ,
        float baseFitRotationX,
        float baseFitRotationZ
) {
    public static final DirectedTurretModelState IDLE = new DirectedTurretModelState(0.0F, 0.0F, 0.0F, 0.0F);

    public static DirectedTurretModelState aimed(float yaw, float pitch, Direction baseDirection) {
        TurretMountRotation.MountAngles mount = TurretMountRotation.forBaseDirection(baseDirection);
        return new DirectedTurretModelState(
                TurretAimHelper.getRotationXYFromYawPitch(yaw, pitch),
                TurretAimHelper.getRotationXZFromYawPitch(yaw, pitch),
                mount.rotationX(),
                mount.rotationZ()
        );
    }
}
