package com.ommods.reopenedmodularturrets;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
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
}
