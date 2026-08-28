package com.ommods.reopenedmodularturrets.core;

public enum MachineMode {
    ALWAYS_ON("always_on"),
    ALWAYS_OFF("always_off"),
    INVERTED("inverted"),
    NON_INVERTED("noninverted");

    private final String name;

    MachineMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MachineMode next() {
        MachineMode[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public static MachineMode fromOrdinal(int ordinal) {
        MachineMode[] values = values();
        if (ordinal < 0 || ordinal >= values.length) {
            return INVERTED;
        }
        return values[ordinal];
    }
}
