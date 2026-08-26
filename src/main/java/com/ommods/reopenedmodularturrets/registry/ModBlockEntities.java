package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import com.ommods.reopenedmodularturrets.blockentity.GrenadeTurretBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.GunTurretBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.SolarAddonBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModConstants.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurretBaseBlockEntity>> TURRET_BASE =
            BLOCK_ENTITIES.register("turret_base", () -> new BlockEntityType<TurretBaseBlockEntity>(
                    (pos, state) -> new TurretBaseBlockEntity(pos, state, ((TurretBaseBlock) state.getBlock()).getTier()),
                    ModBlocks.TURRET_BASE_TIER_1.get(),
                    ModBlocks.TURRET_BASE_TIER_2.get(),
                    ModBlocks.TURRET_BASE_TIER_3.get(),
                    ModBlocks.TURRET_BASE_TIER_4.get(),
                    ModBlocks.TURRET_BASE_TIER_5.get()
            ));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GunTurretBlockEntity>> GUN_TURRET =
            BLOCK_ENTITIES.register("gun_turret", () -> new BlockEntityType<>(GunTurretBlockEntity::new, ModBlocks.GUN_TURRET.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrenadeTurretBlockEntity>> GRENADE_TURRET =
            BLOCK_ENTITIES.register("grenade_turret", () -> new BlockEntityType<>(GrenadeTurretBlockEntity::new, ModBlocks.GRENADE_TURRET.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarAddonBlockEntity>> SOLAR_ADDON =
            BLOCK_ENTITIES.register("solar_addon", () -> new BlockEntityType<>(SolarAddonBlockEntity::new, ModBlocks.SOLAR_ADDON.get()));

    private ModBlockEntities() {}
}
