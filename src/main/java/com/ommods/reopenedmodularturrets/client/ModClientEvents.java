package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.registry.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

@EventBusSubscriber(modid = com.ommods.reopenedmodularturrets.ModConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        event.registerBlockEntityRenderer(ModBlockEntities.TURRET_HEAD.get(), TurretHeadBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SOLAR_ADDON.get(), SolarAddonBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions turretExtensions = new IClientItemExtensions() {
            private TurretItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = TurretItemRenderer.create();
                }
                return renderer;
            }
        };
        IClientItemExtensions solarExtensions = new IClientItemExtensions() {
            private SolarItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = SolarItemRenderer.create();
                }
                return renderer;
            }
        };
        registerTurretItem(event, turretExtensions);
        event.registerItem(solarExtensions, ModItems.SOLAR_ADDON_ITEM.get());
    }

    private static void registerTurretItem(RegisterClientExtensionsEvent event, IClientItemExtensions extensions) {
        event.registerItem(extensions, ModItems.DISPOSABLE_ITEM_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.POTATO_CANNON_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.GUN_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.GRENADE_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.INCENDIARY_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.ROCKET_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.RELATIVISTIC_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.TELEPORTER_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.LASER_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.RAIL_GUN_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.PLASMA_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.ARC_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.MELEE_TURRET_ITEM.get());
        event.registerItem(extensions, ModItems.CROSSBOW_TURRET_ITEM.get());
    }
}
