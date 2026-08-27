package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LootDeleterAddonBlockEntity extends BlockEntity {
    @Nullable
    private TurretBaseBlockEntity base;

    public LootDeleterAddonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LOOT_DELETER_ADDON.get(), pos, state);
    }

    public void bindBase(TurretBaseBlockEntity base) {
        this.base = base;
    }

    @Nullable
    public TurretBaseBlockEntity getBase() {
        return base;
    }
}
