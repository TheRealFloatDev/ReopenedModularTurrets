package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.block.ExpanderInventoryBlock;
import com.ommods.reopenedmodularturrets.block.ExpanderPowerBlock;
import com.ommods.reopenedmodularturrets.block.LeverBlock;
import com.ommods.reopenedmodularturrets.block.LootDeleterAddonBlock;
import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import com.ommods.reopenedmodularturrets.block.TurretHeadBlock;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModConstants.MOD_ID);

    public static final DeferredBlock<TurretBaseBlock.Tier1> TURRET_BASE_TIER_1 =
            BLOCKS.registerBlock("turret_base_tier_1",
                    props -> new TurretBaseBlock.Tier1(applyBaseProperties(props, MapColor.METAL)));
    public static final DeferredBlock<TurretBaseBlock.Tier2> TURRET_BASE_TIER_2 =
            BLOCKS.registerBlock("turret_base_tier_2",
                    props -> new TurretBaseBlock.Tier2(applyBaseProperties(props, MapColor.COLOR_GRAY)));
    public static final DeferredBlock<TurretBaseBlock.Tier3> TURRET_BASE_TIER_3 =
            BLOCKS.registerBlock("turret_base_tier_3",
                    props -> new TurretBaseBlock.Tier3(applyBaseProperties(props, MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<TurretBaseBlock.Tier4> TURRET_BASE_TIER_4 =
            BLOCKS.registerBlock("turret_base_tier_4",
                    props -> new TurretBaseBlock.Tier4(applyBaseProperties(props, MapColor.GOLD)));
    public static final DeferredBlock<TurretBaseBlock.Tier5> TURRET_BASE_TIER_5 =
            BLOCKS.registerBlock("turret_base_tier_5",
                    props -> new TurretBaseBlock.Tier5(applyBaseProperties(props, MapColor.DIAMOND)));

    public static final DeferredBlock<TurretHeadBlock> DISPOSABLE_ITEM_TURRET =
            registerTurretHead(TurretKind.DISPOSABLE_ITEM, "disposable_item_turret", MapColor.COLOR_PURPLE);
    public static final DeferredBlock<TurretHeadBlock> POTATO_CANNON_TURRET =
            registerTurretHead(TurretKind.POTATO_CANNON, "potato_cannon_turret", MapColor.PLANT);
    public static final DeferredBlock<TurretHeadBlock> GUN_TURRET =
            registerTurretHead(TurretKind.GUN, "gun_turret", MapColor.COLOR_RED);
    public static final DeferredBlock<TurretHeadBlock> GRENADE_TURRET =
            registerTurretHead(TurretKind.GRENADE, "grenade_turret", MapColor.TERRACOTTA_ORANGE);
    public static final DeferredBlock<TurretHeadBlock> INCENDIARY_TURRET =
            registerTurretHead(TurretKind.INCENDIARY, "incendiary_turret", MapColor.FIRE);
    public static final DeferredBlock<TurretHeadBlock> ROCKET_TURRET =
            registerTurretHead(TurretKind.ROCKET, "rocket_turret", MapColor.COLOR_RED);
    public static final DeferredBlock<TurretHeadBlock> RELATIVISTIC_TURRET =
            registerTurretHead(TurretKind.RELATIVISTIC, "relativistic_turret", MapColor.COLOR_CYAN);
    public static final DeferredBlock<TurretHeadBlock> TELEPORTER_TURRET =
            registerTurretHead(TurretKind.TELEPORTER, "teleporter_turret", MapColor.COLOR_MAGENTA);
    public static final DeferredBlock<TurretHeadBlock> LASER_TURRET =
            registerTurretHead(TurretKind.LASER, "laser_turret", MapColor.COLOR_RED);
    public static final DeferredBlock<TurretHeadBlock> RAIL_GUN_TURRET =
            registerTurretHead(TurretKind.RAIL_GUN, "rail_gun_turret", MapColor.COLOR_LIGHT_BLUE);
    public static final DeferredBlock<TurretHeadBlock> PLASMA_TURRET =
            registerTurretHead(TurretKind.PLASMA, "plasma_turret", MapColor.COLOR_PINK);
    public static final DeferredBlock<TurretHeadBlock> ARC_TURRET =
            registerTurretHead(TurretKind.ARC, "arc_turret", MapColor.COLOR_YELLOW);
    public static final DeferredBlock<TurretHeadBlock> MELEE_TURRET =
            registerTurretHead(TurretKind.MELEE, "melee_turret", MapColor.STONE);
    public static final DeferredBlock<TurretHeadBlock> CROSSBOW_TURRET =
            registerTurretHead(TurretKind.CROSSBOW, "crossbow_turret", MapColor.WOOD);

    public static final DeferredBlock<LeverBlock> LEVER_BLOCK = BLOCKS.registerBlock(
            "lever_block",
            props -> new LeverBlock(applyBaseProperties(props, MapColor.STONE))
    );

    public static final DeferredBlock<LootDeleterAddonBlock> BASE_ADDON_LOOT_DELETER = BLOCKS.registerBlock(
            "base_addon_loot_deleter",
            props -> new LootDeleterAddonBlock(applyBaseProperties(props, MapColor.COLOR_BLACK))
    );

    public static final DeferredBlock<ExpanderPowerBlock> EXPANDER_POWER_TIER_1 = registerPowerExpander(1);
    public static final DeferredBlock<ExpanderPowerBlock> EXPANDER_POWER_TIER_2 = registerPowerExpander(2);
    public static final DeferredBlock<ExpanderPowerBlock> EXPANDER_POWER_TIER_3 = registerPowerExpander(3);
    public static final DeferredBlock<ExpanderPowerBlock> EXPANDER_POWER_TIER_4 = registerPowerExpander(4);
    public static final DeferredBlock<ExpanderPowerBlock> EXPANDER_POWER_TIER_5 = registerPowerExpander(5);

    public static final DeferredBlock<ExpanderInventoryBlock> EXPANDER_INV_TIER_1 = registerInventoryExpander(1);
    public static final DeferredBlock<ExpanderInventoryBlock> EXPANDER_INV_TIER_2 = registerInventoryExpander(2);
    public static final DeferredBlock<ExpanderInventoryBlock> EXPANDER_INV_TIER_3 = registerInventoryExpander(3);
    public static final DeferredBlock<ExpanderInventoryBlock> EXPANDER_INV_TIER_4 = registerInventoryExpander(4);
    public static final DeferredBlock<ExpanderInventoryBlock> EXPANDER_INV_TIER_5 = registerInventoryExpander(5);

    private static DeferredBlock<TurretHeadBlock> registerTurretHead(TurretKind kind, String name, MapColor color) {
        return BLOCKS.registerBlock(
                name,
                props -> new TurretHeadBlock(kind, applyTurretHeadProperties(props, color))
        );
    }

    private static DeferredBlock<ExpanderPowerBlock> registerPowerExpander(int tier) {
        return BLOCKS.registerBlock(
                "expander_power_tier_" + tier,
                props -> new ExpanderPowerBlock(applyBaseProperties(props, MapColor.METAL))
        );
    }

    private static DeferredBlock<ExpanderInventoryBlock> registerInventoryExpander(int tier) {
        return BLOCKS.registerBlock(
                "expander_inv_tier_" + tier,
                props -> new ExpanderInventoryBlock(applyBaseProperties(props, MapColor.QUARTZ))
        );
    }

    private static BlockBehaviour.Properties applyBaseProperties(BlockBehaviour.Properties props, MapColor color) {
        return props
                .mapColor(color)
                .strength(4.0F, 1200.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops();
    }

    private static BlockBehaviour.Properties applyTurretHeadProperties(BlockBehaviour.Properties props, MapColor color) {
        return applyBaseProperties(props, color).noOcclusion();
    }

    private ModBlocks() {}
}
