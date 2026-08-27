package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.LootDeleterAddonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LootDeleterAddonBlock extends BaseAttachmentBlock {
    public static final MapCodec<LootDeleterAddonBlock> CODEC = simpleCodec(LootDeleterAddonBlock::new);

    public LootDeleterAddonBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends LootDeleterAddonBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LootDeleterAddonBlockEntity(pos, state);
    }
}
