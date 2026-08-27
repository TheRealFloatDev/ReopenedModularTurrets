package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class DirectedTurretRenderState extends BlockEntityRenderState {
    public float rotationX;
    public float rotationZ;
    public TurretKind turretKind;
}
