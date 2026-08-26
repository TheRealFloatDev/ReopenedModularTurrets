package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class ModClientEvents {
    private ModClientEvents() {}

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.GUN_TURRET, GunTurretModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.GRENADE_TURRET, GrenadeTurretModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SOLAR_ADDON, SolarAddonModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GUN_TURRET.get(), GunTurretBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GRENADE_TURRET.get(), GrenadeTurretBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SOLAR_ADDON.get(), SolarAddonBlockRenderer::new);
    }
}
