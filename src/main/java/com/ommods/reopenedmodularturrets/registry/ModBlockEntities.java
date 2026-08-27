package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.block.ExpanderInventoryBlock;
import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import com.ommods.reopenedmodularturrets.blockentity.ExpanderInventoryBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.LeverBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.LootDeleterAddonBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.RedstoneReactorAddonBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.SolarAddonBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModConstants.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurretBaseBlockEntity>> TURRET_BASE =
            BLOCK_ENTITIES.register("turret_base", () -> BlockEntityType.Builder.of(
                    (pos, state) -> new TurretBaseBlockEntity(pos, state, ((TurretBaseBlock) state.getBlock()).getTier()),
                    ModBlocks.TURRET_BASE_TIER_1.get(),
                    ModBlocks.TURRET_BASE_TIER_2.get(),
                    ModBlocks.TURRET_BASE_TIER_3.get(),
                    ModBlocks.TURRET_BASE_TIER_4.get(),
                    ModBlocks.TURRET_BASE_TIER_5.get()
            ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "turret_base")));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurretHeadBlockEntity>> TURRET_HEAD =
            BLOCK_ENTITIES.register("turret_head", () -> BlockEntityType.Builder.of(
                    TurretHeadBlockEntity::new,
                    turretHeadBlocks()
            ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "turret_head")));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarAddonBlockEntity>> SOLAR_ADDON =
            BLOCK_ENTITIES.register("solar_addon", () -> BlockEntityType.Builder.of(
                    SolarAddonBlockEntity::new,
                    ModBlocks.SOLAR_ADDON.get()
            ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "solar_addon")));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneReactorAddonBlockEntity>> REDSTONE_REACTOR_ADDON =
            BLOCK_ENTITIES.register("redstone_reactor_addon", () -> BlockEntityType.Builder.of(
                    RedstoneReactorAddonBlockEntity::new,
                    ModBlocks.REDSTONE_REACTOR_ADDON.get()
            ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "redstone_reactor_addon")));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LootDeleterAddonBlockEntity>> LOOT_DELETER_ADDON =
            BLOCK_ENTITIES.register("loot_deleter_addon", () -> BlockEntityType.Builder.of(
                    LootDeleterAddonBlockEntity::new,
                    ModBlocks.BASE_ADDON_LOOT_DELETER.get()
            ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "loot_deleter_addon")));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LeverBlockEntity>> LEVER_BLOCK =
            BLOCK_ENTITIES.register("lever_block", () -> BlockEntityType.Builder.of(
                    LeverBlockEntity::new,
                    ModBlocks.LEVER_BLOCK.get()
            ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "lever_block")));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExpanderInventoryBlockEntity>> EXPANDER_INVENTORY =
            BLOCK_ENTITIES.register("expander_inventory", () -> BlockEntityType.Builder.of(
                    (pos, state) -> new ExpanderInventoryBlockEntity(
                            pos,
                            state,
                            ((ExpanderInventoryBlock) state.getBlock()).getTier()
                    ),
                    ModBlocks.EXPANDER_INV_TIER_1.get(),
                    ModBlocks.EXPANDER_INV_TIER_2.get(),
                    ModBlocks.EXPANDER_INV_TIER_3.get(),
                    ModBlocks.EXPANDER_INV_TIER_4.get(),
                    ModBlocks.EXPANDER_INV_TIER_5.get()
            ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "expander_inventory")));

    private static Block[] turretHeadBlocks() {
        return new Block[] {
            ModBlocks.DISPOSABLE_ITEM_TURRET.get(),
            ModBlocks.POTATO_CANNON_TURRET.get(),
            ModBlocks.GUN_TURRET.get(),
            ModBlocks.GRENADE_TURRET.get(),
            ModBlocks.INCENDIARY_TURRET.get(),
            ModBlocks.ROCKET_TURRET.get(),
            ModBlocks.RELATIVISTIC_TURRET.get(),
            ModBlocks.TELEPORTER_TURRET.get(),
            ModBlocks.LASER_TURRET.get(),
            ModBlocks.RAIL_GUN_TURRET.get(),
            ModBlocks.PLASMA_TURRET.get(),
            ModBlocks.ARC_TURRET.get(),
            ModBlocks.MELEE_TURRET.get(),
            ModBlocks.CROSSBOW_TURRET.get()
        };
    }

    private ModBlockEntities() {}
}
