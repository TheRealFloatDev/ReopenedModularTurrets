package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.entity.TurretArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TurretArrowRenderer extends ArrowRenderer<TurretArrowEntity> {
    public TurretArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(TurretArrowEntity entity) {
        return ResourceLocation.withDefaultNamespace("textures/entity/projectiles/arrow.png");
    }
}
