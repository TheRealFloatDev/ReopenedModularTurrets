package com.ommods.reopenedmodularturrets.core.targeting;

import com.ommods.reopenedmodularturrets.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class LineOfSightHelper {
    private static final Map<Long, CacheEntry> CACHE = new HashMap<>();
    private static long cacheTick = -1;

    private LineOfSightHelper() {}

    public static boolean canSeeTarget(Level level, BlockPos from, Entity target) {
        if (level.isClientSide()) {
            return true;
        }
        long tick = level.getGameTime();
        if (tick != cacheTick) {
            CACHE.clear();
            cacheTick = tick;
        }
        long key = cacheKey(from, target.getId());
        CacheEntry cached = CACHE.get(key);
        if (cached != null && cached.tick == tick) {
            return cached.visible;
        }
        boolean visible = trace(level, from, target);
        CACHE.put(key, new CacheEntry(tick, visible));
        return visible;
    }

    private static long cacheKey(BlockPos from, int entityId) {
        return ((long) from.asLong() << 32) ^ entityId;
    }

    private static boolean trace(Level level, BlockPos from, Entity target) {
        Vec3 start = Vec3.atCenterOf(from);
        Vec3 end = target.getEyePosition();
        Vec3 delta = end.subtract(start);
        double length = delta.length();
        if (length < 1.0E-4) {
            return true;
        }
        Vec3 step = delta.scale(1.0 / length);
        Vec3 cursor = start;
        int seeThrough = 0;
        int maxSeeThrough = ModConfig.TARGETING_MAX_SEE_THROUGH.get();

        for (int i = 0; i < 256; i++) {
            cursor = cursor.add(step);
            if (cursor.distanceToSqr(end) < 0.25) {
                return true;
            }
            BlockHitResult hit = level.clip(new net.minecraft.world.level.ClipContext(
                    cursor,
                    end,
                    net.minecraft.world.level.ClipContext.Block.COLLIDER,
                    net.minecraft.world.level.ClipContext.Fluid.NONE,
                    target
            ));
            if (hit.getType() == HitResult.Type.MISS) {
                return true;
            }
            if (hit.getType() != HitResult.Type.BLOCK) {
                continue;
            }
            BlockState state = level.getBlockState(hit.getBlockPos());
            if (state.isSolidRender(level, hit.getBlockPos())) {
                return false;
            }
            seeThrough++;
            if (seeThrough > maxSeeThrough) {
                return false;
            }
        }
        return false;
    }

    public static void pruneCache() {
        Iterator<Map.Entry<Long, CacheEntry>> iterator = CACHE.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().tick < cacheTick - 2) {
                iterator.remove();
            }
        }
    }

    private record CacheEntry(long tick, boolean visible) {}
}
