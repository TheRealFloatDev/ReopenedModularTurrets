package com.ommods.openmodularturrets.content.block.turrets;

import com.ommods.omlib.block.OMBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TurretBlockEntity extends OMBlockEntity {
    private final Turret turret;
    private final TurretType turretType;
    public TurretBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, Turret pTurret, TurretType pTurretType) {
        super(pType, pPos, pBlockState);
        this.turret = pTurret;
        this.turretType = pTurretType;
    }

    public Turret getTurret() {
        return turret;
    }

    public TurretType getTurretType() {
        return turretType;
    }
}
