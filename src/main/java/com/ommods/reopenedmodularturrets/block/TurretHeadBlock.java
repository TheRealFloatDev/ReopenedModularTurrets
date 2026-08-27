package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
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
    private static final Map<Block, TurretKind> KIND_BY_BLOCK = new IdentityHashMap<>();

    private final TurretKind kind;

    public TurretHeadBlock(Properties properties) {
        super(properties);
        this.kind = TurretKind.GUN;
    }

    public TurretHeadBlock(TurretKind kind, Properties properties) {
        super(properties);
        this.kind = kind;
    }

    public static void bindKind(Block block, TurretKind kind) {
        KIND_BY_BLOCK.put(block, kind);
    }

    public TurretKind getKind() {
        TurretKind mapped = KIND_BY_BLOCK.get(this);
        return mapped != null ? mapped : kind;
    }

    @Override
    protected MapCodec<? extends TurretHeadBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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
}
