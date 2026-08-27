package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.blockentity.ExpanderInventoryBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExpanderInventoryBlock extends BaseEntityBlock {
    public static final MapCodec<ExpanderInventoryBlock> CODEC = simpleCodec(ExpanderInventoryBlock::new);

    public ExpanderInventoryBlock(Properties properties) {
        super(properties);
    }

    public int getTier() {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(this);
        if (id != null && id.getNamespace().equals(ModConstants.MOD_ID)) {
            String suffix = id.getPath().substring(id.getPath().lastIndexOf('_') + 1);
            try {
                return Integer.parseInt(suffix);
            } catch (NumberFormatException ignored) {
                return 1;
            }
        }
        return 1;
    }

    @Override
    protected MapCodec<? extends ExpanderInventoryBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExpanderInventoryBlockEntity(pos, state, getTier());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return TurretBaseBlock.createTickerHelper(type, ModBlockEntities.EXPANDER_INVENTORY.get(), ExpanderInventoryBlockEntity::serverTick);
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
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ExpanderInventoryBlockEntity expander) {
            refreshAdjacentBase(level, pos, expander);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ExpanderInventoryBlockEntity expander) {
            refreshAdjacentBase(level, pos, expander);
        }
    }

    private static void refreshAdjacentBase(Level level, BlockPos pos, ExpanderInventoryBlockEntity expander) {
        for (net.minecraft.core.Direction direction : net.minecraft.core.Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof TurretBaseBlockEntity base) {
                base.refreshNeighbors();
                expander.bindBase(base);
            }
        }
    }
}
