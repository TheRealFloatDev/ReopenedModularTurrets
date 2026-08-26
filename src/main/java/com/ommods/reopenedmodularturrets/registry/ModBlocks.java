package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.block.GrenadeTurretBlock;
import com.ommods.reopenedmodularturrets.block.GunTurretBlock;
import com.ommods.reopenedmodularturrets.block.SolarAddonBlock;
import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModConstants.MOD_ID);

    public static final DeferredBlock<TurretBaseBlock.Tier1> TURRET_BASE_TIER_1 =
            BLOCKS.registerBlock("turret_base_tier_1", TurretBaseBlock.Tier1::new,
                    props -> applyBaseProperties(props, MapColor.METAL));
    public static final DeferredBlock<TurretBaseBlock.Tier2> TURRET_BASE_TIER_2 =
            BLOCKS.registerBlock("turret_base_tier_2", TurretBaseBlock.Tier2::new,
                    props -> applyBaseProperties(props, MapColor.COLOR_GRAY));
    public static final DeferredBlock<TurretBaseBlock.Tier3> TURRET_BASE_TIER_3 =
            BLOCKS.registerBlock("turret_base_tier_3", TurretBaseBlock.Tier3::new,
                    props -> applyBaseProperties(props, MapColor.COLOR_LIGHT_GRAY));
    public static final DeferredBlock<TurretBaseBlock.Tier4> TURRET_BASE_TIER_4 =
            BLOCKS.registerBlock("turret_base_tier_4", TurretBaseBlock.Tier4::new,
                    props -> applyBaseProperties(props, MapColor.GOLD));
    public static final DeferredBlock<TurretBaseBlock.Tier5> TURRET_BASE_TIER_5 =
            BLOCKS.registerBlock("turret_base_tier_5", TurretBaseBlock.Tier5::new,
                    props -> applyBaseProperties(props, MapColor.DIAMOND));

    public static final DeferredBlock<GunTurretBlock> GUN_TURRET = BLOCKS.registerBlock(
            "gun_turret",
            GunTurretBlock::new,
            props -> applyBaseProperties(props, MapColor.COLOR_RED)
    );

    public static final DeferredBlock<GrenadeTurretBlock> GRENADE_TURRET = BLOCKS.registerBlock(
            "grenade_turret",
            GrenadeTurretBlock::new,
            props -> applyBaseProperties(props, MapColor.TERRACOTTA_ORANGE)
    );

    public static final DeferredBlock<SolarAddonBlock> SOLAR_ADDON = BLOCKS.registerBlock(
            "addon_solar",
            SolarAddonBlock::new,
            props -> applyBaseProperties(props, MapColor.COLOR_YELLOW)
    );

    private static BlockBehaviour.Properties applyBaseProperties(BlockBehaviour.Properties props, MapColor color) {
        return props
                .mapColor(color)
                .strength(4.0F, 1200.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops();
    }

    private ModBlocks() {}
}
