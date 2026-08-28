package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.entity.PlasmaProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PlasmaProjectileRenderer extends TurretProjectileRenderer<PlasmaProjectileEntity> {
    public PlasmaProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, "plasma");
    }
}
