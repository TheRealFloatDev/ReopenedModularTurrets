package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.reference.Constants;

public class ArtilleryShellItem extends AmmoItem{
    public static final String NAME = Constants.Items.AmmoArtilleryShell;
    public ArtilleryShellItem(Properties properties) {
        super(AmmoType.ARTILLERY_SHELL, properties);
    }

    @Override
    public int getMagazineSize() {
        return 0;
    }
}
