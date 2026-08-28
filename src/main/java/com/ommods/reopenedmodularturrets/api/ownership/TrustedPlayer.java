package com.ommods.reopenedmodularturrets.api.ownership;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TrustedPlayer {
    private String name;
    private AccessLevel accessLevel = AccessLevel.NONE;
    @Nullable
    private UUID uuid;

    public TrustedPlayer(String name) {
        this.name = name;
    }

    public TrustedPlayer(String name, AccessLevel accessLevel) {
        this.name = name;
        this.accessLevel = accessLevel;
    }

    public static TrustedPlayer fromNbt(CompoundTag tag) {
        TrustedPlayer player = new TrustedPlayer(tag.getString("name"));
        player.accessLevel = AccessLevel.fromLevel(tag.getInt("accessLevel"));
        if (tag.hasUUID("uuid")) {
            player.uuid = tag.getUUID("uuid");
        }
        return player;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putInt("accessLevel", accessLevel.getLevel());
        if (uuid != null) {
            tag.putUUID("uuid", uuid);
        }
        return tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Nullable
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@Nullable UUID uuid) {
        this.uuid = uuid;
    }

    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("accessLevel", accessLevel.getLevel());
        if (uuid != null) {
            map.put("uuid", uuid.toString());
        }
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrustedPlayer that)) {
            return false;
        }
        return accessLevel == that.accessLevel
                && Objects.equals(name, that.name)
                && Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accessLevel, uuid);
    }
}
