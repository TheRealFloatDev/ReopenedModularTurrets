package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public final class TurretMountRotation {
    private TurretMountRotation() {}

    public record MountAngles(float rotationX, float rotationZ) {
        public static final MountAngles NONE = new MountAngles(0.0F, 0.0F);
    }

    public static MountAngles forBaseDirection(@Nullable Direction baseDirection) {
        if (baseDirection == null) {
            return MountAngles.NONE;
        }
        return switch (baseDirection) {
            case EAST -> new MountAngles(1.56F, 1.565F);
            case WEST -> new MountAngles(1.56F, 4.705F);
            case SOUTH -> new MountAngles(1.56F, 3.145F);
            case NORTH -> new MountAngles(1.56F, 0.0F);
            case UP -> new MountAngles(3.145F, 0.0F);
            case DOWN -> new MountAngles(0.0F, 0.0F);
        };
    }
}
