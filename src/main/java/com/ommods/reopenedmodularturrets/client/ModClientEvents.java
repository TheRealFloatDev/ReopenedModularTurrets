package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.client.special.GrenadeTurretSpecialRenderer;
import com.ommods.reopenedmodularturrets.client.special.GunTurretSpecialRenderer;
import com.ommods.reopenedmodularturrets.client.special.SolarAddonSpecialRenderer;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

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

    @SubscribeEvent
    public static void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(
                Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "gun_turret"),
                GunTurretSpecialRenderer.Unbaked.MAP_CODEC
        );
        event.register(
                Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "grenade_turret"),
                GrenadeTurretSpecialRenderer.Unbaked.MAP_CODEC
        );
        event.register(
                Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "addon_solar"),
                SolarAddonSpecialRenderer.Unbaked.MAP_CODEC
        );
    }
}
