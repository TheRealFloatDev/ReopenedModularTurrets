package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.block.AddonAttachmentHelper;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RedstoneReactorAddonBlockEntity extends BlockEntity {
    @Nullable
    private TurretBaseBlockEntity base;

    public RedstoneReactorAddonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REDSTONE_REACTOR_ADDON.get(), pos, state);
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
        if (base == null) {
            return;
        }
        int signal = level.getBestNeighborSignal(worldPosition);
        if (signal > 0) {
            int generation = signal * 4;
            int current = base.getEnergyStorage().getEnergyStored();
            int capacity = base.getEffectiveMaxEnergy();
            base.getEnergyStorage().receiveEnergy(Math.min(capacity - current, generation), false);
            base.setChanged();
        }
    }
}
