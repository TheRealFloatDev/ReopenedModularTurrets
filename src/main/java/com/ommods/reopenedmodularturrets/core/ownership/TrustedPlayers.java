package com.ommods.reopenedmodularturrets.core.ownership;

import com.ommods.reopenedmodularturrets.api.ownership.AccessLevel;
import com.ommods.reopenedmodularturrets.api.ownership.TrustedPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TrustedPlayers {
    private final List<TrustedPlayer> players = new ArrayList<>();

    public List<TrustedPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public List<String> getNames() {
        return players.stream().map(TrustedPlayer::getName).toList();
    }

    public boolean isTrusted(String playerName) {
        return getTrusted(playerName).isPresent();
    }

    public Optional<TrustedPlayer> getTrusted(String playerName) {
        for (TrustedPlayer player : players) {
            if (player.getName().equalsIgnoreCase(playerName)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public boolean add(String playerName) {
        return add(new TrustedPlayer(playerName.trim(), AccessLevel.NONE));
    }

    public boolean add(TrustedPlayer trustedPlayer) {
        String trimmed = trustedPlayer.getName().trim();
        if (trimmed.isEmpty() || isTrusted(trimmed)) {
            return false;
        }
        players.add(new TrustedPlayer(trimmed, trustedPlayer.getAccessLevel()));
        return true;
    }

    public boolean remove(String playerName) {
        return players.removeIf(player -> player.getName().equalsIgnoreCase(playerName));
    }

    public boolean changeAccessLevel(String playerName, AccessLevel level) {
        Optional<TrustedPlayer> trusted = getTrusted(playerName);
        if (trusted.isEmpty()) {
            return false;
        }
        trusted.get().setAccessLevel(level);
        return true;
    }

    public void setPlayers(List<TrustedPlayer> newPlayers) {
        players.clear();
        for (TrustedPlayer player : newPlayers) {
            add(player);
        }
    }

    public List<Map<String, Object>> asListMap() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TrustedPlayer player : players) {
            list.add(player.asMap());
        }
        return list;
    }

    public void save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (TrustedPlayer player : players) {
            list.add(player.toNbt());
        }
        tag.put("TrustedPlayersV2", list);
        ListTag legacy = new ListTag();
        for (TrustedPlayer player : players) {
            legacy.add(StringTag.valueOf(player.getName()));
        }
        tag.put("TrustedPlayers", legacy);
    }

    public void load(CompoundTag tag) {
        players.clear();
        if (tag.contains("TrustedPlayersV2", Tag.TAG_LIST)) {
            ListTag list = tag.getList("TrustedPlayersV2", Tag.TAG_COMPOUND);
            for (Tag entry : list) {
                TrustedPlayer player = TrustedPlayer.fromNbt((CompoundTag) entry);
                if (!player.getName().isEmpty() && !isTrusted(player.getName())) {
                    players.add(player);
                }
            }
            return;
        }
        if (!tag.contains("TrustedPlayers", Tag.TAG_LIST)) {
            return;
        }
        ListTag list = tag.getList("TrustedPlayers", Tag.TAG_STRING);
        for (Tag entry : list) {
            String name = entry.getAsString().trim();
            if (!name.isEmpty() && !isTrusted(name)) {
                players.add(new TrustedPlayer(name, AccessLevel.NONE));
            }
        }
    }

    public CompoundTag saveToTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        save(tag);
        return tag;
    }

    public void loadFromTag(CompoundTag tag, HolderLookup.Provider registries) {
        load(tag);
    }
}
