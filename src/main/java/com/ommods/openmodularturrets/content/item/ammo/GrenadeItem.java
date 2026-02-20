package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class GrenadeItem extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoGrenade;
    public GrenadeItem(Properties properties) {
        super(AmmoType.GRENADE, properties);
    }

    @Override
    public int getMagazineSize() {
        return 0;
    }
}
