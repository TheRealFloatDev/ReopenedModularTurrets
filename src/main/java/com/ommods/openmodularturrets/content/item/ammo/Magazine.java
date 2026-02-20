package com.ommods.openmodularturrets.content.item.ammo;

public class Magazine {
    private final AmmoType ammoType;
    private final int ammoCapacity;

    public Magazine(AmmoType ammoType, int ammoCapacity) {
        this.ammoType = ammoType;
        this.ammoCapacity = ammoCapacity;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    public int getAmmoCapacity() {
        return ammoCapacity;
    }

}
