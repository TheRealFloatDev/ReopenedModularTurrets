package com.ommods.reopenedmodularturrets.block;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class AddonAttachmentHelper {
    private AddonAttachmentHelper() {}

    @Nullable
    public static Direction findBaseFacing(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockState(neighborPos).getBlock() instanceof TurretBaseBlock) {
                return direction;
            }
        }
        return null;
    }

    @Nullable
    public static TurretBaseBlockEntity findAdjacentBase(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof TurretBaseBlockEntity base) {
                return base;
            }
        }
        return null;
    }

    @Nullable
    public static Direction findHorizontalTierOneBaseFacing(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            if (neighbor instanceof TurretBaseBlockEntity base && base.getTier() == 1) {
                return direction;
            }
        }
        return null;
    }

    public static boolean canAttachToBase(LevelReader level, BlockPos pos) {
        return findBaseFacing(level, pos) != null;
    }

    @Nullable
    public static BlockState applyBaseFacing(BlockState state, Direction facing) {
        if (!state.hasProperty(BaseAttachmentBlock.FACING)) {
            return state;
        }
        return state.setValue(BaseAttachmentBlock.FACING, facing);
    }

    @Nullable
    public static BlockState getAttachmentStateForPlacement(BlockPlaceContext context, BlockState defaultState) {
        Direction facing = findBaseFacing(context.getLevel(), context.getClickedPos());
        if (facing == null) {
            return null;
        }
        return applyBaseFacing(defaultState, facing);
    }

    public static void refreshAdjacentBases(LevelAccessor level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }
        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof TurretBaseBlockEntity base) {
                base.refreshNeighbors();
            }
        }
    }
}
