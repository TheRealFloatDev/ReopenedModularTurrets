package com.ommods.openmodularturrets.content.block.turret_bases;

import com.ommods.openmodularturrets.config.OMTConfig;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class TurretBaseBlockTier2 extends TurretBaseBlock {
    public static final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.METAL)
            .destroyTime(OMTConfig.TurretBase.baseTier2DestroySpeed)
            .explosionResistance(OMTConfig.TurretBase.baseTier2BlastResistance);

    public TurretBaseBlockTier2(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected int getTier() {
        return 2;
    }
}
