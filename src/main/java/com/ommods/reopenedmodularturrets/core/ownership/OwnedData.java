package com.ommods.reopenedmodularturrets.core.ownership;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;

public final class OwnedData {
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);

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
        if (player instanceof ServerPlayer serverPlayer
                && serverPlayer.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(2)))) {
            return true;
        }
        return player.getUUID().equals(ownerUuid);
    }

    public void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        if (ownerUuid != null) {
            output.store("OwnerUUID", UUID_CODEC, ownerUuid);
        }
        if (ownerName != null) {
            output.putString("OwnerName", ownerName);
        }
    }

    public void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        ownerUuid = input.read("OwnerUUID", UUID_CODEC).orElse(null);
        ownerName = input.getStringOr("OwnerName", null);
    }
}
