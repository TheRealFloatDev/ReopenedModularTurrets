package com.ommods.openmodularturrets.content.item.ammo;

import com.ommods.openmodularturrets.OMTItems;
import net.minecraft.world.item.Item;

public enum AmmoType {
    BULLET("bullet",    OMTItems.ammoItemBullet.get()),
    ROCKET_SMALL("rocket_small",    OMTItems.ammoItemRocketSmall.get()),
    ROCKET_LARGE("rocket_large",    OMTItems.ammoItemBullet.get()),
    RAILGUN_SLUG("railgun_slug",    OMTItems.ammoItemBullet.get()),
    CROSSBOW_BOLT("crossbow_bolt",    OMTItems.ammoItemBullet.get()),
    FIRE_CHARGE("fire_charge",    OMTItems.ammoItemBullet.get()),
    GRENADE("grenade",    OMTItems.ammoItemBullet.get()),
    ARTILLERY_SHELL("artillery_shell",    OMTItems.ammoItemBullet.get()),
    NONE("none",    null),; // For energy based turrets

    private final String name;
    private final Item ammoItem;

    AmmoType(String name, Item ammoItem) {
        this.name = name;
        this.ammoItem = ammoItem;
    }

    public AmmoItem getAmmoItem() {
        return (AmmoItem) ammoItem;
    }

    public static AmmoType getType(String name) {
        for (AmmoType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return NONE;
    }
}
