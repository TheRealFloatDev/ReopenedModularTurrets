package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.LeverBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LeverBlock extends BaseEntityBlock {
    public static final MapCodec<LeverBlock> CODEC = simpleCodec(LeverBlock::new);

    public LeverBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<LeverBlock> codec() {
        return CODEC;
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

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof TurretBaseBlockEntity base && base.getTier() == 1) {
                int generation = ModConfig.LEVER_GENERATION.get();
                int current = base.getEnergyStorage().getEnergyStored();
                int capacity = base.getEffectiveMaxEnergy();
                base.getEnergyStorage().receiveEnergy(Math.min(capacity - current, generation), false);
                base.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }
}
