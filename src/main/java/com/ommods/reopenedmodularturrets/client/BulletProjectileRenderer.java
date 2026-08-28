package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.entity.BulletProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BulletProjectileRenderer extends TurretProjectileRenderer<BulletProjectileEntity> {
    public BulletProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, "ammo_bullet");
    }
}
