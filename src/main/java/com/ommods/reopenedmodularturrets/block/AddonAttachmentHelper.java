package com.ommods.reopenedmodularturrets.block;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class AddonAttachmentHelper {
    private AddonAttachmentHelper() {}

    public static void onAddonPlaced(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }
        refreshAdjacentBases(level, pos);
        if (!hasAdjacentBase(level, pos)) {
            Block.popResource(level, pos, new ItemStack(state.getBlock()));
            level.removeBlock(pos, false);
        }
    }

    public static void refreshAdjacentBases(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof TurretBaseBlockEntity base) {
                base.refreshNeighbors();
            }
        }
    }

    public static boolean hasAdjacentBase(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (level.getBlockEntity(pos.relative(direction)) instanceof TurretBaseBlockEntity) {
                return true;
            }
        }
        return false;
    }
}
