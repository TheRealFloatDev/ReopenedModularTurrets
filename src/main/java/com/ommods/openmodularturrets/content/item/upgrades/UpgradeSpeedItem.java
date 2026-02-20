package com.ommods.openmodularturrets.content.item.upgrades;

import com.ommods.openmodularturrets.config.OMTConfig;

public class UpgradeSpeedItem  extends  AbstractUpgradeItem{
    public UpgradeSpeedItem(Properties properties) {
        super(properties);
        this.effect = UpgradeEffect.SPEED;
    }
}
