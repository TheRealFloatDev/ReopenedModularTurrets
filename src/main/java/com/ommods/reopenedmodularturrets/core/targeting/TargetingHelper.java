package com.ommods.reopenedmodularturrets.core.targeting;

import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.core.BlockPos;
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
import java.util.function.Predicate;

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
            UUID ownerUuid,
            List<String> trustedPlayers
    ) {
        return findTarget(level, origin, BlockPos.containing(origin), range, downRange,
                attackMobs, attackPlayers, attackNeutral, ownerUuid, trustedPlayers,
                TurretKind.GUN, null, null);
    }

    public static Optional<LivingEntity> findTarget(
            Level level,
            Vec3 origin,
            BlockPos aimFrom,
            double range,
            double downRange,
            boolean attackMobs,
            boolean attackPlayers,
            boolean attackNeutral,
            UUID ownerUuid,
            List<String> trustedPlayers,
            TurretKind kind,
            @org.jetbrains.annotations.Nullable LivingEntity exclude,
            @org.jetbrains.annotations.Nullable Predicate<LivingEntity> extraFilter
    ) {
        double vertical = range + downRange;
        AABB box = new AABB(
                origin.x - range, origin.y - downRange, origin.z - range,
                origin.x + range, origin.y + vertical, origin.z + range
        );
        int[] priorities = kind.getDefaultPriorities();
        List<LivingEntity> candidates = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != exclude
                        && isValidTarget(entity, attackMobs, attackPlayers, attackNeutral, ownerUuid, trustedPlayers)
                        && (extraFilter == null || extraFilter.test(entity))
        );

        return candidates.stream()
                .filter(entity -> horizontalDistance(origin, entity.position()) <= range)
                .filter(entity -> entity.getY() >= origin.y - downRange && entity.getY() <= origin.y + vertical)
                .filter(entity -> LineOfSightHelper.canSeeTarget(level, aimFrom, entity))
                .max(Comparator.comparingDouble(entity -> TargetingPriorities.score(entity, origin, priorities)));
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
            UUID ownerUuid,
            List<String> trustedPlayers
    ) {
        if (!entity.isAlive() || entity.isRemoved()) {
            return false;
        }
        if (entity instanceof Player player) {
            if (ownerUuid != null && player.getUUID().equals(ownerUuid)) {
                return false;
            }
            if (isTrusted(trustedPlayers, player.getName().getString())) {
                return false;
            }
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

    private static boolean isTrusted(List<String> trustedPlayers, String playerName) {
        for (String trusted : trustedPlayers) {
            if (trusted.equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean[] activeFilters() {
        return new boolean[] {
                ModConfig.ATTACK_MOBS.get(),
                ModConfig.ATTACK_PLAYERS.get(),
                ModConfig.ATTACK_NEUTRAL.get()
        };
    }
}
