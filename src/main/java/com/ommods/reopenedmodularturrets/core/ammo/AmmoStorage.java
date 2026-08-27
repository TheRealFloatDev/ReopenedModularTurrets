package com.ommods.reopenedmodularturrets.core.ammo;

import com.ommods.reopenedmodularturrets.item.AmmoType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class AmmoStorage {
    private final int maxCapacity;
    private final AmmoType[] slotTypes = new AmmoType[5];
    private final int[] slotCounts = new int[5];

    public AmmoStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCount(AmmoType type) {
        for (int i = 0; i < slotTypes.length; i++) {
            if (slotTypes[i] == type) {
                return slotCounts[i];
            }
        }
        return 0;
    }

    public boolean tryConsume(AmmoType type, int amount) {
        for (int i = 0; i < slotTypes.length; i++) {
            if (slotTypes[i] == type && slotCounts[i] >= amount) {
                slotCounts[i] -= amount;
                return true;
            }
        }
        return false;
    }

    public boolean tryInsertStack(ItemStack stack) {
        AmmoType type = AmmoType.fromItem(stack);
        if (type == null) {
            return false;
        }
        int perItem = type.getAmmoPerItem();
        int totalAmmo = perItem * stack.getCount();
        int index = findOrAssignSlot(type);
        if (index < 0) {
            return false;
        }
        int space = maxCapacity - slotCounts[index];
        if (space <= 0) {
            return false;
        }
        int acceptedAmmo = Math.min(space, totalAmmo);
        slotCounts[index] += acceptedAmmo;
        int consumedItems = (acceptedAmmo + perItem - 1) / perItem;
        stack.shrink(consumedItems);
        return true;
    }

    private int findOrAssignSlot(AmmoType type) {
        for (int i = 0; i < slotTypes.length; i++) {
            if (slotTypes[i] == type) {
                return i;
            }
        }
        for (int i = 0; i < slotTypes.length; i++) {
            if (slotTypes[i] == null) {
                slotTypes[i] = type;
                return i;
            }
        }
        return -1;
    }

    public void save(CompoundTag tag) {
        for (int i = 0; i < slotTypes.length; i++) {
            if (slotTypes[i] != null) {
                tag.putString("AmmoType" + i, slotTypes[i].name());
                tag.putInt("AmmoCount" + i, slotCounts[i]);
            }
        }
    }

    public void load(CompoundTag tag) {
        for (int i = 0; i < slotTypes.length; i++) {
            String typeName = tag.contains("AmmoType" + i) ? tag.getString("AmmoType" + i) : null;
            if (typeName != null && !typeName.isEmpty()) {
                slotTypes[i] = AmmoType.valueOf(typeName);
                slotCounts[i] = tag.getInt("AmmoCount" + i);
            } else {
                slotTypes[i] = null;
                slotCounts[i] = 0;
            }
        }
    }
}
