package com.ommods.reopenedmodularturrets.core.targeting;

import com.ommods.reopenedmodularturrets.config.ModConfig;
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
import java.util.UUID;

public final class TargetingHelper {
    private TargetingHelper() {}

    public static Optional<LivingEntity> findTarget(
            Level level,
            Vec3 origin,
            double range,
            double downRange,
            boolean attackMobs,
            boolean attackPlayers,
            boolean attackNeutral,
            UUID ownerUuid
    ) {
        double vertical = range + downRange;
        AABB box = new AABB(
                origin.x - range, origin.y - downRange, origin.z - range,
                origin.x + range, origin.y + vertical, origin.z + range
        );
        List<LivingEntity> candidates = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> isValidTarget(entity, attackMobs, attackPlayers, attackNeutral, ownerUuid)
        );

        return candidates.stream()
                .filter(entity -> horizontalDistance(origin, entity.position()) <= range)
                .filter(entity -> entity.getY() >= origin.y - downRange && entity.getY() <= origin.y + vertical)
                .min(Comparator.comparingDouble(entity -> entity.position().distanceToSqr(origin)));
    }

    private static double horizontalDistance(Vec3 origin, Vec3 target) {
        double dx = target.x - origin.x;
        double dz = target.z - origin.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static boolean isValidTarget(
            LivingEntity entity,
            boolean attackMobs,
            boolean attackPlayers,
            boolean attackNeutral,
            UUID ownerUuid
    ) {
        if (!entity.isAlive() || entity.isRemoved()) {
            return false;
        }
        if (ownerUuid != null && entity instanceof Player player && player.getUUID().equals(ownerUuid)) {
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
