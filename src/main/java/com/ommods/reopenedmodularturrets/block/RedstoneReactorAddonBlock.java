package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.RedstoneReactorAddonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RedstoneReactorAddonBlock extends BaseEntityBlock {
    public static final MapCodec<RedstoneReactorAddonBlock> CODEC = simpleCodec(RedstoneReactorAddonBlock::new);

    public RedstoneReactorAddonBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends RedstoneReactorAddonBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneReactorAddonBlockEntity(pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        AddonAttachmentHelper.onAddonPlaced(level, pos, state);
    }

    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            BlockPos fromPos,
            boolean isMoving
    ) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        AddonAttachmentHelper.refreshAdjacentBases(level, pos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        AddonAttachmentHelper.refreshAdjacentBases(level, pos);
        return super.playerWillDestroy(level, pos, state, player);
    }
}
