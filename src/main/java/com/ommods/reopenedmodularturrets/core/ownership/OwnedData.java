package com.ommods.reopenedmodularturrets.core.ownership;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;

public final class OwnedData {
    private UUID ownerUuid;
    private String ownerName;

    public Optional<UUID> getOwnerUuid() {
        return Optional.ofNullable(ownerUuid);
    }

    public String getOwnerName() {
        return ownerName == null ? "" : ownerName;
    }

    public void setOwner(Player player) {
        this.ownerUuid = player.getUUID();
        this.ownerName = player.getName().getString();
    }

    public boolean canAccess(Player player) {
        if (ownerUuid == null) {
            return true;
        }
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.hasPermissions(2)) {
            return true;
        }
        return player.getUUID().equals(ownerUuid);
    }

    public void save(CompoundTag tag) {
        if (ownerUuid != null) {
            tag.putUUID("OwnerUUID", ownerUuid);
        }
        if (ownerName != null) {
            tag.putString("OwnerName", ownerName);
        }
    }

    public void load(CompoundTag tag) {
        if (tag.hasUUID("OwnerUUID")) {
            ownerUuid = tag.getUUID("OwnerUUID");
        } else {
            ownerUuid = null;
        }
        ownerName = tag.contains("OwnerName") ? tag.getString("OwnerName") : null;
    }
}
