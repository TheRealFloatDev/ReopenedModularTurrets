package com.ommods.reopenedmodularturrets.core.targeting;

public enum TargetFilter {
    MOBS,
    PLAYERS,
    NEUTRAL;

    public static TargetFilter fromIndex(int index) {
        TargetFilter[] values = values();
        if (index < 0 || index >= values.length) {
            return MOBS;
        }
        return values[index];
    }
}
