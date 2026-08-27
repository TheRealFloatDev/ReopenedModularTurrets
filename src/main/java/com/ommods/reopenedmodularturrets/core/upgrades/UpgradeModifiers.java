package com.ommods.reopenedmodularturrets.core.upgrades;

public record UpgradeModifiers(
        float fireRateMultiplier,
        float energyMultiplier,
        float rangeMultiplier,
        float accuracyMultiplier,
        boolean scatterShot
) {
    public static final UpgradeModifiers NONE = new UpgradeModifiers(1.0F, 1.0F, 1.0F, 1.0F, false);

    public int applyCooldown(int baseCooldown) {
        return Math.max(1, Math.round(baseCooldown / fireRateMultiplier));
    }

    public int applyEnergyCost(int baseCost) {
        return Math.max(1, Math.round(baseCost * energyMultiplier));
    }

    public double applyRange(double baseRange) {
        return baseRange * rangeMultiplier;
    }

    public float applySpread(float baseSpread) {
        return baseSpread / accuracyMultiplier;
    }
}
