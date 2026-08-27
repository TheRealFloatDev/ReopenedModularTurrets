package com.ommods.reopenedmodularturrets.core.addons;

public record AddonState(
        boolean solar,
        boolean redstoneReactor,
        boolean lootDeleter,
        boolean damageAmp,
        boolean potentia,
        boolean recycler,
        boolean concealer,
        boolean fakeDrops,
        boolean serialPort
) {
    public static final AddonState EMPTY = new AddonState(false, false, false, false, false, false, false, false, false);

    public float damageMultiplier() {
        return damageAmp ? 1.5F : 1.0F;
    }

    public float energyMultiplier() {
        return potentia ? 0.75F : 1.0F;
    }

    public float recycleChance() {
        return recycler ? 0.25F : 0.0F;
    }
}
