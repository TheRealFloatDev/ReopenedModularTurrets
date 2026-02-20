package com.ommods.openmodularturrets.content.block.turret_bases;

import com.ommods.openmodularturrets.config.OMTConfig;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class TurretBaseBlockTier4 extends TurretBaseBlock {
    public static final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.METAL)
            .destroyTime(OMTConfig.TurretBase.baseTier4DestroySpeed)
            .explosionResistance(OMTConfig.TurretBase.baseTier4BlastResistance);

    public TurretBaseBlockTier4(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected int getTier() {
        return 4;
    }
}
