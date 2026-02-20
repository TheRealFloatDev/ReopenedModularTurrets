package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class BulletItem  extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoBullet;
    public BulletItem(Properties properties) {
        super(AmmoType.BULLET, properties);
    }

    @Override
    public int getMagazineSize() {
        return 64;
    }
}
