package com.ommods.openmodularturrets;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OMTCreativeModeTab extends CreativeModeTab {

    public OMTCreativeModeTab() {
        super("omt");
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return null; // TODO: Ad// d turret base itemstack
    }
}
