package com.ommods.openmodularturrets.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OMTConfig {
    public static ForgeConfigSpec serverConfigSpec;
    public static ForgeConfigSpec commonConfigSpec;



    public static class General {
        public static String recipes;
    }

    public static class Permission {
        public static Boolean canOPAccessOwnedBlocks;
        public static Boolean offlineModeSupport;
    }

    public static class Debug {

        public static Boolean doDebugChat;
        public static Boolean debugLogging;

    }
    
    public static class TurretBase {
        public static int baseTier1MaxCharge;
        public static int baseTier1MaxTransferRate;
        public static int baseTier1BlastResistance;
        public static int baseTier1DestroySpeed;
        public static int baseTier2MaxCharge;
        public static int baseTier2MaxTransferRate;
        public static int baseTier2BlastResistance;
        public static int baseTier2DestroySpeed;
        public static int baseTier3MaxCharge;
        public static int baseTier3MaxTransferRate;
        public static int baseTier3BlastResistance;
        public static int baseTier3DestroySpeed;
        public static int baseTier4MaxCharge;
        public static int baseTier4MaxTransferRate;
        public static int baseTier4BlastResistance;
        public static int baseTier4DestroySpeed;
        public static int baseTier5MaxCharge;
        public static int baseTier5MaxTransferRate;
        public static int baseTier5BlastResistance;
        public static int baseTier5DestroySpeed;
    }


    static {
        Pair<ServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder()
                .configure(ServerConfig::new);
        Pair<CommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder()
                .configure(CommonConfig::new);


        // Store pair values in some constant field
        serverConfigSpec = serverPair.getValue();
        commonConfigSpec = commonPair.getValue();

        General.recipes = serverConfigSpec.get("general.recipes");
        Permission.canOPAccessOwnedBlocks = serverConfigSpec.get("canOPAccessOwnedBlocks");
        Permission.offlineModeSupport = serverConfigSpec.get("offlineModeSupport");

        Debug.doDebugChat = commonConfigSpec.get("doDebugChat");
        Debug.debugLogging = commonConfigSpec.get("debugLogging");

        TurretBase.baseTier1MaxCharge = serverConfigSpec.get("baseTier1MaxCharge");
        TurretBase.baseTier1MaxTransferRate = serverConfigSpec.get("baseTier1MaxTransferRate");
        TurretBase.baseTier1BlastResistance = serverConfigSpec.get("baseTier1BlastResistance");
        TurretBase.baseTier1DestroySpeed = serverConfigSpec.get("baseTier1DestroySpeed");

        TurretBase.baseTier2MaxCharge = serverConfigSpec.get("baseTier2MaxCharge");
        TurretBase.baseTier2MaxTransferRate = serverConfigSpec.get("baseTier2MaxTransferRate");
        TurretBase.baseTier2BlastResistance = serverConfigSpec.get("baseTier2BlastResistance");
        TurretBase.baseTier2DestroySpeed = serverConfigSpec.get("baseTier2DestroySpeed");

        TurretBase.baseTier3MaxCharge = serverConfigSpec.get("baseTier3MaxCharge");
        TurretBase.baseTier3MaxTransferRate = serverConfigSpec.get("baseTier3MaxTransferRate");
        TurretBase.baseTier3BlastResistance = serverConfigSpec.get("baseTier3BlastResistance");
        TurretBase.baseTier3DestroySpeed = serverConfigSpec.get("baseTier3DestroySpeed");

        TurretBase.baseTier4MaxCharge = serverConfigSpec.get("baseTier4MaxCharge");
        TurretBase.baseTier4MaxTransferRate = serverConfigSpec.get("baseTier4MaxTransferRate");
        TurretBase.baseTier4BlastResistance = serverConfigSpec.get("baseTier4BlastResistance");
        TurretBase.baseTier4DestroySpeed = serverConfigSpec.get("baseTier4DestroySpeed");

        TurretBase.baseTier5MaxCharge = serverConfigSpec.get("baseTier5MaxCharge");
        TurretBase.baseTier5MaxTransferRate = serverConfigSpec.get("baseTier5MaxTransferRate");
        TurretBase.baseTier5BlastResistance = serverConfigSpec.get("baseTier5BlastResistance");
        TurretBase.baseTier5DestroySpeed = serverConfigSpec.get("baseTier5DestroySpeed");
    }
}
