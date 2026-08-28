package com.ommods.reopenedmodularturrets.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class LimitedStackSlot extends Slot {
    private final Predicate<ItemStack> filter;
    private final int maxStackSize;

    public LimitedStackSlot(Container container, int index, int x, int y, Predicate<ItemStack> filter, int maxStackSize) {
        super(container, index, x, y);
        this.filter = filter;
        this.maxStackSize = maxStackSize;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return filter.test(stack);
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return maxStackSize;
    }
}
