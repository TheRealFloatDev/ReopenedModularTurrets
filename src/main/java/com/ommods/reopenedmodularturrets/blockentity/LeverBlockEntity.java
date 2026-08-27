package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LeverBlockEntity extends BlockEntity {
    public LeverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LEVER_BLOCK.get(), pos, state);
    }
}
