package com.ommods.openmodularturrets.content.item.ammo;

public class AmmoDropEvent {
    private AmmoType ammoType;
    private int count;

    public AmmoDropEvent(AmmoType ammoType, int count) {
        this.ammoType = ammoType;
        this.count = count;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    public int getCount() {
        return count;
    }
}
