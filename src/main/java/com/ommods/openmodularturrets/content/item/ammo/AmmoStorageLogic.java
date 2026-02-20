package com.ommods.openmodularturrets.content.item.ammo;

import net.minecraft.world.item.ItemStack;

public class AmmoStorageLogic {
    private AmmoType[] supportedAmmoTypes = new AmmoType[2];
    private int[] ammoCount = new int[2];

    private final int MAX_STORAGE_CAPACITY;

    public AmmoStorageLogic(int storageCapacity) {
        this.MAX_STORAGE_CAPACITY = storageCapacity;

    }

    // Method to insert magazines
    public MagazineInsertResult tryInsertMagazines(ItemStack magazineStack, Magazine magazineType) {
        // Find the index for this ammo type
        int typeIndex = findAmmoTypeIndex(magazineType.getAmmoType());
        if (typeIndex == -1) {
            // Unsupported type, return unchanged
            return new MagazineInsertResult(false, magazineStack);
        }

        // Calculate free space
        int freeSpace = MAX_STORAGE_CAPACITY - ammoCount[typeIndex];

        // Calculate total potential ammo from magazines
        int totalMagazineAmmo = magazineType.getAmmoCapacity() * magazineStack.getCount();

        // Check if all magazines can be inserted
        if (freeSpace >= totalMagazineAmmo) {
            // Consume all magazines
            ammoCount[typeIndex] += totalMagazineAmmo;

            // Return empty stack
            return new MagazineInsertResult(true, ItemStack.EMPTY);
        } else {
            // Cannot insert all magazines
            return new MagazineInsertResult(false, magazineStack);
        }
    }

    // Try to consume ammo
    public boolean tryConsumeAmmo(AmmoType ammoType, int amount) {
        int typeIndex = findAmmoTypeIndex(ammoType);

        if (typeIndex == -1 || ammoCount[typeIndex] < amount) {
            return false;
        }

        // Consume ammo
        ammoCount[typeIndex] -= amount;
        return true;
    }

    // Find the index of a supported ammo type
    private int findAmmoTypeIndex(AmmoType ammoType) {
        for (int i = 0; i < supportedAmmoTypes.length; i++) {
            if (supportedAmmoTypes[i] == ammoType) {
                return i;
            }
        }
        return -1;
    }

    // Get current ammo count for a type
    public int getAmmoCount(AmmoType ammoType) {
        int index = findAmmoTypeIndex(ammoType);
        return index != -1 ? ammoCount[index] : 0;
    }

    // Get supported ammo types
    public AmmoType[] getSupportedAmmoTypes() {
        return supportedAmmoTypes;
    }

    // Result class for magazine insertion
    public static class MagazineInsertResult {
        public final boolean accepted;
        public final ItemStack remainingMagazineStack;

        public MagazineInsertResult(boolean accepted, ItemStack remainingMagazineStack) {
            this.accepted = accepted;
            this.remainingMagazineStack = remainingMagazineStack;
        }
    }

    public void setSupportedAmmoTypes(AmmoType type, int slot) {
        if (slot < 0 || type == null || slot >= supportedAmmoTypes.length) {
            return;
        }
        if (ammoCount[slot] > 0) {


        }
        supportedAmmoTypes[slot] = type;
    }
}