package com.ommods.reopenedmodularturrets.core.ownership;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TrustedPlayers {
    private final List<String> names = new ArrayList<>();

    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    public boolean isTrusted(String playerName) {
        for (String name : names) {
            if (name.equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(String playerName) {
        String trimmed = playerName.trim();
        if (trimmed.isEmpty() || isTrusted(trimmed)) {
            return false;
        }
        names.add(trimmed);
        return true;
    }

    public boolean remove(String playerName) {
        return names.removeIf(name -> name.equalsIgnoreCase(playerName));
    }

    public void save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (String name : names) {
            list.add(StringTag.valueOf(name));
        }
        tag.put("TrustedPlayers", list);
    }

    public void load(CompoundTag tag) {
        names.clear();
        if (!tag.contains("TrustedPlayers", Tag.TAG_LIST)) {
            return;
        }
        ListTag list = tag.getList("TrustedPlayers", Tag.TAG_STRING);
        for (Tag entry : list) {
            String name = entry.getAsString().trim();
            if (!name.isEmpty() && !isTrusted(name)) {
                names.add(name);
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
