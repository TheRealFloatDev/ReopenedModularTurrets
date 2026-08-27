package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LeverBlock extends Block {
    public static final MapCodec<LeverBlock> CODEC = simpleCodec(LeverBlock::new);

    public LeverBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<LeverBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            level.blockEvent(pos, this, 1, 0);
        }
        return InteractionResult.SUCCESS;
    }
}
