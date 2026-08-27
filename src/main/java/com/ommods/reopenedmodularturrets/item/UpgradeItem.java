package com.ommods.reopenedmodularturrets.item;

import net.minecraft.world.item.Item;

public class UpgradeItem extends Item {
    private final Type type;

    public UpgradeItem(Type type, Properties properties) {
        super(properties);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        FIRE_RATE,
        EFFICIENCY,
        RANGE,
        ACCURACY,
        SCATTER_SHOT
    }
}
