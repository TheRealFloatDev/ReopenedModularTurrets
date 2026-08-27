package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.RedstoneReactorAddonBlockEntity;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneReactorAddonBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return TurretBaseBlock.createTickerHelper(type, ModBlockEntities.REDSTONE_REACTOR_ADDON.get(), RedstoneReactorAddonBlockEntity::serverTick);
    }
}
