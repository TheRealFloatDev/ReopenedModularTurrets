package com.ommods.reopenedmodularturrets.client.model;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.resources.ResourceLocation;
import com.ommods.reopenedmodularturrets.turret.TurretKind;

public final class ModEntityTextures {
    public static final ResourceLocation SOLAR_ADDON = texture("addon_solar");

    private ModEntityTextures() {}

    public static ResourceLocation forTurret(TurretKind kind) {
        return texture(kind.textureName());
    }

    public static ResourceLocation texture(String name) {
        return ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/entity/" + name + ".png");
    }
}
