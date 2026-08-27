package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TurretHeadBlock extends BaseEntityBlock {
    public static final MapCodec<TurretHeadBlock> CODEC = simpleCodec(TurretHeadBlock::new);

    private final TurretKind kind;

    public TurretHeadBlock(Properties properties) {
        super(properties);
        this.kind = TurretKind.GUN;
    }

    public TurretHeadBlock(TurretKind kind, Properties properties) {
        super(properties);
        this.kind = kind;
    }

    public TurretKind getKind() {
        return kind;
    }

    @Override
    protected MapCodec<? extends TurretHeadBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TurretHeadBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return TurretBaseBlock.createTickerHelper(type, ModBlockEntities.TURRET_HEAD.get(), TurretHeadBlockEntity::serverTick);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide()) {
            return;
        }
        refreshAdjacentBase(level, pos);
        if (!validateTier(level, pos)) {
            Block.popResource(level, pos, new ItemStack(this));
            level.removeBlock(pos, false);
        }
    }

    private static boolean validateTier(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof TurretBaseBlockEntity base
                    && level.getBlockEntity(pos) instanceof TurretHeadBlockEntity head) {
                return base.getTier() >= head.getKind().getMinTier();
            }
        }
        return true;
    }

    private static void refreshAdjacentBase(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof TurretBaseBlockEntity base) {
                base.refreshNeighbors();
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        refreshAdjacentBase(level, pos);
        return super.playerWillDestroy(level, pos, state, player);
    }
}
