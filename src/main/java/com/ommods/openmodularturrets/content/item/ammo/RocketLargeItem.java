package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class RocketLargeItem extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoRocketLarge;
    public RocketLargeItem(Properties properties) {
        super(AmmoType.ROCKET_LARGE, properties);
    }

    @Override
    public int getMagazineSize() {
        return 0;
    }
}
