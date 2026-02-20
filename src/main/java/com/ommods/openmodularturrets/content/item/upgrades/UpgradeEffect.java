package com.ommods.openmodularturrets.content.item.upgrades;

public enum UpgradeEffect {
    SPEED("Speed", "Increases the speed of the base's turrets", 0.05),
    RANGE("Range", "Increases the range of the base's turrets", 0.05),
    AMMO_CAPACITY("Ammo Capacity", "Increases the ammo capacity of the base", 0.25),
    ACCURACY("Accuracy", "Increases the accuracy of the base's turrets", 0.05);
    //TODO: add localization
    final String name;
    final String description;
    double value;

    public static final UpgradeEffect[] VALUES = values();

    public static UpgradeEffect getByOrdinal(int ordinal) {
        return VALUES[ordinal];
    }

    public static int getOrdinal(UpgradeEffect effect) {
        return effect.ordinal();
    }

    UpgradeEffect(String name, String description, double value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }
}
