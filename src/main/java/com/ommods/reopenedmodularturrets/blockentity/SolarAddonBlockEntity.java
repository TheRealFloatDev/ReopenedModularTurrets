package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.block.AddonAttachmentHelper;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SolarAddonBlockEntity extends BlockEntity {
    @Nullable
    private TurretBaseBlockEntity base;

    public SolarAddonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOLAR_ADDON.get(), pos, state);
    }

    public void bindBase(TurretBaseBlockEntity base) {
        this.base = base;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide()) {
            AddonAttachmentHelper.refreshAdjacentBases(level, worldPosition);
        }
    }

    public void tickGeneration(ServerLevel level) {
        if (base == null || !level.canSeeSky(worldPosition)) {
            return;
        }
        int generation = ModConfig.SOLAR_GENERATION.get();
        int current = base.getEnergyStorage().getEnergyStored();
        int capacity = base.getEnergyStorage().getMaxEnergyStored();
        base.getEnergyStorage().receiveEnergy(Math.min(capacity - current, generation), false);
        base.setChanged();
    }
}
