package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class CrossbowBoltItem extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoCrossbowBolt;
    public CrossbowBoltItem(Properties properties) {
        super(AmmoType.CROSSBOW_BOLT, properties);
    }

    @Override
    public int getMagazineSize() {
        return 64;
    }
}
