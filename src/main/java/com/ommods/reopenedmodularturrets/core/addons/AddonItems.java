package com.ommods.reopenedmodularturrets.core.addons;

import com.ommods.reopenedmodularturrets.registry.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AddonItems {
    private AddonItems() {}

    public static boolean isAddonItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        return item == ModItems.ADDON_DAMAGE_AMP.get()
                || item == ModItems.ADDON_POTENTIA.get()
                || item == ModItems.ADDON_RECYCLER.get()
                || item == ModItems.ADDON_CONCEALER.get()
                || item == ModItems.ADDON_FAKE_DROPS.get()
                || item == ModItems.ADDON_SERIAL_PORT.get()
                || item == ModItems.SOLAR_ADDON_ITEM.get()
                || item == ModItems.REDSTONE_REACTOR_ADDON_ITEM.get();
    }
}
