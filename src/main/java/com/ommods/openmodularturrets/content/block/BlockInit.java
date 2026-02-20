package com.ommods.openmodularturrets.content.block;

import com.ommods.openmodularturrets.content.block.turret_bases.*;
import com.ommods.openmodularturrets.reference.Constants;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.level.material.Material;

import static com.ommods.openmodularturrets.OpenModularTurrets.REGISTRATE;

public class BlockInit {
    public static final RegistryEntry<TurretBaseBlockTier1> TurretBaseTier1 = REGISTRATE.get().block(Constants.Blocks.TURRET_BASE_TIER_1, TurretBaseBlockTier1::new).initialProperties(Material.METAL).register();
    public static final RegistryEntry<TurretBaseBlockTier2> TurretBaseTier2 = REGISTRATE.get().block(Constants.Blocks.TURRET_BASE_TIER_2, TurretBaseBlockTier2::new).register();
    public static final RegistryEntry<TurretBaseBlockTier3> TurretBaseTier3 = REGISTRATE.get().block(Constants.Blocks.TURRET_BASE_TIER_3, TurretBaseBlockTier3::new).register();
    public static final RegistryEntry<TurretBaseBlockTier4> TurretBaseTier4 = REGISTRATE.get().block(Constants.Blocks.TURRET_BASE_TIER_4, TurretBaseBlockTier4::new).register();
    public static final RegistryEntry<TurretBaseBlockTier5> TurretBaseTier5 = REGISTRATE.get().block(Constants.Blocks.TURRET_BASE_TIER_5, TurretBaseBlockTier5::new).register();
}
