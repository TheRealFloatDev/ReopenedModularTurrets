package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class RailgunSlugItem extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoRailgunSlug;
    public RailgunSlugItem(Properties properties) {
        super(AmmoType.RAILGUN_SLUG, properties);
    }

    @Override
    public int getMagazineSize() {
        return 64;
    }
}
