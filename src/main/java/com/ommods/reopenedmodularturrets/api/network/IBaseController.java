package com.ommods.reopenedmodularturrets.api.network;

import com.ommods.reopenedmodularturrets.api.ownership.TrustedPlayer;
import com.ommods.reopenedmodularturrets.api.targeting.TargetingSettings;
import com.ommods.reopenedmodularturrets.core.MachineMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface IBaseController extends IController {
    boolean isEntityValidTarget(Entity target, float yaw, float pitch);

    boolean overridesMode();

    MachineMode getOverriddenMode();

    BlockPos getPosition();

    TargetingSettings getTargetingSettings();

    List<TrustedPlayer> getTrustedPlayerList();
}
