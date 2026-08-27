package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;

public class BaseAddonBlock extends Block {
    public static final MapCodec<BaseAddonBlock> CODEC = simpleCodec(BaseAddonBlock::new);

    public BaseAddonBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<BaseAddonBlock> codec() {
        return CODEC;
    }
}
