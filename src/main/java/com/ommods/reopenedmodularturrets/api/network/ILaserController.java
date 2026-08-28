package com.ommods.reopenedmodularturrets.api.network;

import com.ommods.reopenedmodularturrets.api.render.LaserColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface ILaserController extends IController {
    boolean isEntityValidTarget(Entity target, float yaw, float pitch);

    boolean overridesMode();

    BlockPos getPositionOfBlock();

    LaserColor getColorForLaser();
}
