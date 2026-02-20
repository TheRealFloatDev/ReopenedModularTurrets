package com.ommods.openmodularturrets.content.block.turrets;

import com.ommods.openmodularturrets.content.block.turret_bases.TurretBaseLogic;
import net.minecraft.core.BlockPos;

public class Turret {
    private final TurretBaseLogic turretBaseLogic;
    private final BlockPos pos;
    private final TurretType turretType;


    public Turret(TurretBaseLogic turretBaseLogic, BlockPos pos, TurretType turretType) {
        this.turretBaseLogic = turretBaseLogic;
        this.pos = pos;
        this.turretType = turretType;
    }

    public BlockPos getPos() {
        return pos;
    }

    public TurretType getTurretType() {
        return turretType;
    }
}
