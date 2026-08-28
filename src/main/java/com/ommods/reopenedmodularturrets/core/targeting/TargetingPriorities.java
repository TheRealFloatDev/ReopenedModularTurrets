package com.ommods.reopenedmodularturrets.core.targeting;

import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class TargetingPriorities {
    public enum Axis {
        MAX_HP,
        HP_REMAINING,
        DISTANCE,
        ARMOR,
        PLAYER
    }

    private TargetingPriorities() {}

    public static double score(LivingEntity entity, Vec3 origin, int[] weights) {
        if (weights.length < 5) {
            return -entity.distanceToSqr(origin);
        }
        double maxHealth = entity.getMaxHealth();
        double health = entity.getHealth();
        double distance = entity.position().distanceTo(origin);
        double armor = entity.getAttributeValue(Attributes.ARMOR);
        double player = entity instanceof Player ? 1.0 : 0.0;

        return weights[0] * maxHealth
                + weights[1] * health
                + weights[2] * (-distance)
                + weights[3] * armor
                + weights[4] * player;
    }
}
