package com.ommods.reopenedmodularturrets.api.targeting;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class TargetingSettings {
    private boolean targetPlayers;
    private boolean targetMobs;
    private boolean targetPassive;
    private int range;
    private int maxRange;

    public TargetingSettings(boolean targetPlayers, boolean targetMobs, boolean targetPassive, int range, int maxRange) {
        this.targetPlayers = targetPlayers;
        this.targetMobs = targetMobs;
        this.targetPassive = targetPassive;
        this.range = range;
        this.maxRange = maxRange;
    }

    public static TargetingSettings defaults() {
        return new TargetingSettings(false, true, false, 16, 16);
    }

    public static TargetingSettings readFromNbt(CompoundTag tag) {
        if (tag.contains("targetingSettings")) {
            CompoundTag settings = tag.getCompound("targetingSettings");
            return new TargetingSettings(
                    settings.getBoolean("targetPlayers"),
                    settings.getBoolean("targetMobs"),
                    settings.getBoolean("targetPassive"),
                    settings.getInt("range"),
                    settings.getInt("maxRange")
            );
        }
        return new TargetingSettings(
                tag.getBoolean("AttackPlayers"),
                tag.getBoolean("AttackMobs"),
                tag.getBoolean("AttackNeutral"),
                tag.getInt("TargetRange"),
                tag.getInt("TargetRange")
        );
    }

    public CompoundTag writeToNbt(CompoundTag tag) {
        CompoundTag settings = new CompoundTag();
        settings.putBoolean("targetPlayers", targetPlayers);
        settings.putBoolean("targetMobs", targetMobs);
        settings.putBoolean("targetPassive", targetPassive);
        settings.putInt("range", range);
        settings.putInt("maxRange", maxRange);
        tag.put("targetingSettings", settings);
        return tag;
    }

    public boolean isTargetPlayers() {
        return targetPlayers;
    }

    public TargetingSettings setTargetPlayers(boolean targetPlayers) {
        this.targetPlayers = targetPlayers;
        return this;
    }

    public boolean isTargetMobs() {
        return targetMobs;
    }

    public TargetingSettings setTargetMobs(boolean targetMobs) {
        this.targetMobs = targetMobs;
        return this;
    }

    public boolean isTargetPassive() {
        return targetPassive;
    }

    public TargetingSettings setTargetPassive(boolean targetPassive) {
        this.targetPassive = targetPassive;
        return this;
    }

    public int getRange() {
        return range;
    }

    public TargetingSettings setRange(int range) {
        if (range <= maxRange && range > -1) {
            this.range = range;
        }
        return this;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public TargetingSettings setMaxRange(int maxRange) {
        this.maxRange = maxRange;
        if (this.maxRange < this.range) {
            this.range = this.maxRange;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TargetingSettings that)) {
            return false;
        }
        return targetPlayers == that.targetPlayers
                && targetMobs == that.targetMobs
                && targetPassive == that.targetPassive
                && range == that.range
                && maxRange == that.maxRange;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetPlayers, targetMobs, targetPassive, range, maxRange);
    }
}
