package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class FireChargeItem extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoFireCharge;
    public FireChargeItem(Properties properties) {
        super(AmmoType.FIRE_CHARGE, properties);
    }

    @Override
    public int getMagazineSize() {
        return 64;
    }
}
