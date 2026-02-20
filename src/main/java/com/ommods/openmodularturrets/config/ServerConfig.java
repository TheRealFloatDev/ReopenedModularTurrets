package com.ommods.openmodularturrets.config;


import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;

public class ServerConfig {

     TurretSettings gunTurretSettings;
     TurretSettings laserTurretSettings;
     TurretSettings rocketSmallTurretSettings;
     TurretSettings rocketLargeTurretSettings;
     TurretSettings flameTurretSettings;
     TurretSettings grenadeLauncherTurretSettings;
     TurretSettings railgunTurretSettings;
     TurretSettings disposableTurretSettings;
     TurretSettings meleeTurretSettings;
     TurretSettings crossbowTurretSettings;
     TurretSettings artilleryTurretSettings;
     TurretSettings plasmaLauncherTurretSettings;
     TurretSettings teleporterTurretSettings;
     TurretSettings relativisticTurretSettings;




    // In some config class
    ServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("GLOBAL_TARGETING");
        // Define values here in final fields
        final ForgeConfigSpec.ConfigValue<Boolean> canTurretsAttackMobs =
                builder.comment("Can turrets attack mobs?")
                        .define("canTurretsAttackMobs", true);
        final ForgeConfigSpec.ConfigValue<Boolean> canTurretsAttackPlayers =
                builder.comment("Can turrets attack players?")
                        .define("canTurretsAttackPlayers", true);
        final ForgeConfigSpec.ConfigValue<Boolean> canTurretsAttackNeutralMobs =
                builder.comment("Can turrets attack neutral passives?")
                        .define("canTurretsAttackPassives", true);
        builder.pop();
        builder.push("GENERAL");
        final ForgeConfigSpec.ConfigValue<String> recipes =
                builder.comment("Which recipes to use. Valid values: auto,vanilla")
                        .defineInList("recipes", "auto", Arrays.asList("auto", "vanilla"));
        builder.pop();
        builder.push("BASE_SETTINGS"  );
        final ForgeConfigSpec.ConfigValue<Integer> baseTier1MaxCharge = builder.comment("Base max charge for tier 1 turrets")
                .defineInRange("baseTier1MaxCharge", 500, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier1BlastResistance = builder.comment("Base blast resistance for tier 1 turrets")
                .defineInRange("baseTier1BlastResistance", 10, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier1DestroySpeed = builder.comment("Base destroy speed for tier 1 turrets")
                .defineInRange("baseTier1DestroySpeed", 10, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier2MaxCharge = builder.comment("Base max charge for tier 2 turrets")
                .defineInRange("baseTier2MaxCharge", 50000, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier2BlastResistance = builder.comment("Base blast resistance for tier 2 turrets")
                .defineInRange("baseTier1BlastResistance", 20, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier2DestroySpeed = builder.comment("Base destroy speed for tier 2 turrets")
                .defineInRange("baseTier1DestroySpeed", 20, 0, Integer.MAX_VALUE);
        final   ForgeConfigSpec.ConfigValue<Integer> baseTier3MaxCharge = builder.comment("Base max charge for tier 3 turrets")
                .defineInRange("baseTier3MaxCharge", 150000, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier3BlastResistance = builder.comment("Base blast resistance for tier 3 turrets")
                .defineInRange("baseTier3BlastResistance", 30, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier3DestroySpeed = builder.comment("Base destroy speed for tier 3 turrets")
                .defineInRange("baseTier3DestroySpeed", 30, 0, Integer.MAX_VALUE);
        final       ForgeConfigSpec.ConfigValue<Integer> baseTier4MaxCharge = builder.comment("Base max charge for tier 4 turrets")
                .defineInRange("baseTier4MaxCharge", 500000, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier4BlastResistance = builder.comment("Base blast resistance for tier 4 turrets")
                .defineInRange("baseTier4BlastResistance", 40, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier4DestroySpeed = builder.comment("Base destroy speed for tier 4 turrets")
                .defineInRange("baseTier4DestroySpeed", 40, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier5MaxCharge = builder.comment("Base max charge for tier 5 turrets")
                .defineInRange("baseTier5MaxCharge", 1000000, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier5BlastResistance = builder.comment("Base blast resistance for tier 5 turrets")
                .defineInRange("baseTier5BlastResistance", 50, 0, Integer.MAX_VALUE);
        final ForgeConfigSpec.ConfigValue<Integer> baseTier5DestroySpeed = builder.comment("Base destroy speed for tier 5 turrets")
                .defineInRange("baseTier5DestroySpeed", 50, 0, Integer.MAX_VALUE);

        builder.push("TURRET_SETTINGS");
        gunTurretSettings = new TurretSettings(builder, "gunTurret",1,1,1,1,1);
        // TODO get config from 1.12 version
        builder.pop();
    }
}

