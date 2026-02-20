package com.ommods.openmodularturrets.content.item.ammo;

import net.minecraft.world.item.Item;

public abstract class AmmoItem extends Item {
    private final AmmoType ammoType;

    protected AmmoItem(AmmoType ammoType, Properties properties) {
        super(properties);
        this.ammoType = ammoType;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    public boolean isValidAmmoForTurret(AmmoType ammoType) {
        return this.ammoType == ammoType;
    }

    // Return 0 if the ammo cannot be used in a magazine
    public abstract int getMagazineSize();
}
