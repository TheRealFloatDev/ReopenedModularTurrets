package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class RocketSmallItem extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoRocketSmall;
    public RocketSmallItem(Properties properties) {
        super(AmmoType.ROCKET_SMALL, properties);
    }

    @Override
    public int getMagazineSize() {
        return 64;
    }
}
