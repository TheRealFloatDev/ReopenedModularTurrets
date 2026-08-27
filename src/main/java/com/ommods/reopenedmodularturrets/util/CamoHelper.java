package com.ommods.reopenedmodularturrets.util;

import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;

public final class CamoHelper {
    private CamoHelper() {}

    public static boolean canApplyCamo(ItemStack stack, Level level, BlockPos pos) {
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
            return false;
        }
        return isValidCamoBlock(blockItem.getBlock(), blockItem.getBlock().defaultBlockState(), level, pos);
    }

    public static boolean isValidCamoBlock(Block block, BlockState state, Level level, BlockPos pos) {
        if (state.isAir()) {
            return false;
        }
        if (block instanceof TurretBaseBlock || block instanceof BaseEntityBlock) {
            return false;
        }
        if (block.defaultBlockState().hasBlockEntity()) {
            return false;
        }
        return state.getCollisionShape(level, pos).equals(Shapes.block());
    }
}
