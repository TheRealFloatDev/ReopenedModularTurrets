package com.ommods.openmodularturrets.content.item.upgrades;

import net.minecraft.world.item.Item;

public abstract class AbstractUpgradeItem extends Item {
    public UpgradeEffect effect;

    public AbstractUpgradeItem(Properties pProperties) {
        super(pProperties);
    }

    public UpgradeEffect getEffect() {
        return effect;
    }
}
