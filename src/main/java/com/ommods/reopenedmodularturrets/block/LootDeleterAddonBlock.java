package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.LootDeleterAddonBlockEntity;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LootDeleterAddonBlock extends BaseEntityBlock {
    public static final MapCodec<LootDeleterAddonBlock> CODEC = simpleCodec(LootDeleterAddonBlock::new);

    public LootDeleterAddonBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends LootDeleterAddonBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LootDeleterAddonBlockEntity(pos, state);
    }
}
