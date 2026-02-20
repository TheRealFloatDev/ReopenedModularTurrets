package com.ommods.openmodularturrets.content.block.turret_bases;

import com.ommods.openmodularturrets.config.OMTConfig;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class TurretBaseBlockTier5 extends TurretBaseBlock {
    public static final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.METAL)
            .destroyTime(OMTConfig.TurretBase.baseTier5DestroySpeed)
            .explosionResistance(OMTConfig.TurretBase.baseTier5BlastResistance);

    public TurretBaseBlockTier5(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected int getTier() {
        return 5;
    }
}
