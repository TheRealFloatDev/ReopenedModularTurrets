package com.ommods.openmodularturrets.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TurretSettings {
    public final String name;
    public final ForgeConfigSpec.ConfigValue<Integer> powerUsage;
    public final ForgeConfigSpec.ConfigValue<Integer> range;
    public final ForgeConfigSpec.ConfigValue<Double> fireRate;
    public final ForgeConfigSpec.ConfigValue<Double> damage;
    public final ForgeConfigSpec.ConfigValue<Double> damageAddonModifierMaxHP;

    public TurretSettings(ForgeConfigSpec.Builder builder, String name, int powerUsage, int range, double fireRate, double damage, double damageAddonModifierMaxHP) {
        this.name = name;
        builder.push(name);
        this. powerUsage = builder.comment("Power usage for " + name).defineInRange("power_usage", powerUsage,0, Integer.MAX_VALUE);
        this.range = builder.comment("Range for " + name).defineInRange("range", range, 0, Integer.MAX_VALUE);
        this.fireRate = builder.comment("Fire rate for " + name).defineInRange("fire_rate", fireRate, 0, Double.MAX_VALUE);
        this.damage = builder.comment("Damage for " + name).defineInRange("damage", damage, 0, Double.MAX_VALUE);
        this.damageAddonModifierMaxHP = builder.comment("Damage addon modifier max HP for " + name).defineInRange("damage_addon_modifier_max_hp", damageAddonModifierMaxHP, 0, Double.MAX_VALUE);
        builder.pop();
    }

    // For non-damaging turrets
    public TurretSettings(ForgeConfigSpec.Builder builder, String name, int powerUsage, int range, double fireRate) {
        this.name = name;
        builder.push(name);
        this. powerUsage = builder.comment("Power usage for " + name).defineInRange("power_usage", powerUsage,0, Integer.MAX_VALUE);
        this.range = builder.comment("Range for " + name).defineInRange("range", range, 0, Integer.MAX_VALUE);
        this.fireRate = builder.comment("Fire rate for " + name).defineInRange("fire_rate", fireRate, 0, Double.MAX_VALUE);
        this.damage = builder.comment("[NOT USED] Damage for " + name).defineInRange("damage", 0D, 0D, 0D);
        this.damageAddonModifierMaxHP = builder.comment("[NOT USED] Damage addon modifier max HP for " + name).defineInRange("damage_addon_modifier_max_hp", 0D, 0D, 0D);
        builder.pop();
    }
}