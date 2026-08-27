package com.ommods.reopenedmodularturrets.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum AmmoType {
    BULLET(8),
    GRENADE(1),
    BLAZING_CLAY(4),
    FERRO_SLUG(2),
    ROCKET(1);

    private final int ammoPerItem;

    AmmoType(int ammoPerItem) {
        this.ammoPerItem = ammoPerItem;
    }

    public int getAmmoPerItem() {
        return ammoPerItem;
    }

    public static AmmoType fromItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        Item item = stack.getItem();
        if (item instanceof AmmoItem ammoItem) {
            return ammoItem.getAmmoType();
        }
        return null;
    }
}
