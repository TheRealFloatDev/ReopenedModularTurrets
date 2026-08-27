package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ExpanderPowerBlock extends Block {
    public static final MapCodec<ExpanderPowerBlock> CODEC = simpleCodec(ExpanderPowerBlock::new);

    public ExpanderPowerBlock(Properties properties) {
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
    protected MapCodec<? extends ExpanderPowerBlock> codec() {
        return CODEC;
    }
}
