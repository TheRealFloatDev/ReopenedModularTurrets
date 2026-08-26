package com.ommods.reopenedmodularturrets.core.targeting;

import com.ommods.reopenedmodularturrets.config.ModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class TargetingHelper {
    private TargetingHelper() {}

    public static Optional<LivingEntity> findTarget(
            Level level,
            Vec3 origin,
            double range,
            boolean attackMobs,
            boolean attackPlayers,
            boolean attackNeutral
    ) {
        AABB box = AABB.ofSize(origin, range * 2, range * 2, range * 2);
        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, box, entity -> isValidTarget(entity, attackMobs, attackPlayers, attackNeutral));

        return candidates.stream()
                .filter(entity -> entity.position().distanceTo(origin) <= range)
                .min(Comparator.comparingDouble(entity -> entity.position().distanceToSqr(origin)));
    }

    public static boolean isValidTarget(LivingEntity entity, boolean attackMobs, boolean attackPlayers, boolean attackNeutral) {
        if (!entity.isAlive() || entity.isRemoved()) {
            return false;
        }
        if (entity instanceof Player player) {
            return attackPlayers && !player.isCreative() && !player.isSpectator();
        }
        if (entity instanceof Animal) {
            return attackNeutral;
        }
        if (entity instanceof Enemy || entity instanceof Mob) {
            return attackMobs;
        }
        return attackMobs;
    }

    public static boolean[] activeFilters() {
        return new boolean[] {
                ModConfig.ATTACK_MOBS.get(),
                ModConfig.ATTACK_PLAYERS.get(),
                ModConfig.ATTACK_NEUTRAL.get()
        };
    }
}
