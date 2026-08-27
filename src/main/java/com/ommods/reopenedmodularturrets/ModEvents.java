package com.ommods.reopenedmodularturrets;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public final class ModEvents {
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player
                && event.getLevel().getBlockEntity(event.getPos()) instanceof TurretBaseBlockEntity base) {
            if (base.getOwnedData().getOwnerUuid().isEmpty()) {
                base.getOwnedData().setOwner(player);
                base.setChanged();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        for (TurretBaseBlockEntity base : findNearbyBases(event.getEntity().level(), event.getEntity().blockPosition())) {
            if (base.shouldDeleteLoot()) {
                event.getDrops().clear();
                event.setCanceled(true);
                return;
            }
        }
    }

    private static Iterable<TurretBaseBlockEntity> findNearbyBases(net.minecraft.world.level.Level level, BlockPos origin) {
        java.util.List<TurretBaseBlockEntity> bases = new java.util.ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (level.getBlockEntity(origin.offset(x, y, z)) instanceof TurretBaseBlockEntity base) {
                        bases.add(base);
                    }
                }
            }
        }
        return bases;
    }
}
