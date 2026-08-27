package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.LeverBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LeverBlock extends BaseEntityBlock {
    public static final MapCodec<LeverBlock> CODEC = simpleCodec(LeverBlock::new);
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public LeverBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ROTATION, 0));
    }

    @Override
    protected MapCodec<LeverBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LeverBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction baseFacing = AddonAttachmentHelper.findHorizontalTierOneBaseFacing(context.getLevel(), context.getClickedPos());
        if (baseFacing == null) {
            return null;
        }
        return defaultBlockState().setValue(ROTATION, rotationForBaseFacing(baseFacing));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return AddonAttachmentHelper.findHorizontalTierOneBaseFacing(level, pos) != null;
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
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return TurretBaseBlock.createTickerHelper(type, ModBlockEntities.LEVER_BLOCK.get(), LeverBlockEntity::tick);
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        InteractionResult crankResult = crank(state, level, pos, player, hitResult);
        if (crankResult != InteractionResult.PASS) {
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return crank(state, level, pos, player, hitResult);
    }

    private InteractionResult crank(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof LeverBlockEntity lever)) {
            return InteractionResult.PASS;
        }
        if (lever.isTurning()) {
            return InteractionResult.SUCCESS;
        }
        lever.startCrank();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        Direction baseFacing = AddonAttachmentHelper.findHorizontalTierOneBaseFacing(level, pos);
        if (baseFacing == null) {
            return InteractionResult.PASS;
        }
        BlockEntity neighbor = level.getBlockEntity(pos.relative(baseFacing));
        if (neighbor instanceof TurretBaseBlockEntity base) {
            int generation = ModConfig.LEVER_GENERATION.get();
            int current = base.getEnergyStorage().getEnergyStored();
            int capacity = base.getEffectiveMaxEnergy();
            base.getEnergyStorage().receiveEnergy(Math.min(capacity - current, generation), false);
            base.setChanged();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static int rotationForBaseFacing(Direction baseFacing) {
        return switch (baseFacing) {
            case SOUTH -> 0;
            case WEST -> 4;
            case NORTH -> 8;
            case EAST -> 12;
            default -> 0;
        };
    }
}
