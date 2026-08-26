package com.ommods.reopenedmodularturrets.item;

import net.minecraft.world.item.Item;

public class AmmoItem extends Item {
    private final AmmoType ammoType;

    public AmmoItem(AmmoType ammoType, Item.Properties properties) {
        super(properties);
        this.ammoType = ammoType;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }
}
