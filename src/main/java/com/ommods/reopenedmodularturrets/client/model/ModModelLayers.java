package com.ommods.reopenedmodularturrets.client.model;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class ModModelLayers {
    public static final ModelLayerLocation GUN_TURRET =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "gun_turret"), "main");
    public static final ModelLayerLocation GRENADE_TURRET =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "grenade_turret"), "main");
    public static final ModelLayerLocation SOLAR_ADDON =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "addon_solar"), "main");

    private ModModelLayers() {}
}
