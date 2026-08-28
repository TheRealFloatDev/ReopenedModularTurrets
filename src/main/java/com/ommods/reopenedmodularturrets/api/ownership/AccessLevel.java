package com.ommods.reopenedmodularturrets.api.ownership;

public enum AccessLevel {
    NONE(0),
    ACCESS(1),
    MODIFY(2),
    ADMIN(3);

    private final int level;

    AccessLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static AccessLevel fromLevel(int level) {
        for (AccessLevel value : values()) {
            if (value.level == level) {
                return value;
            }
        }
        return NONE;
    }

    public AccessLevel next() {
        return fromLevel((level + 1) % 4);
    }
}
