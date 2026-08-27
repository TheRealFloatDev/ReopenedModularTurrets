package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.LeverBlockModel;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.ProjectileModel;
import com.ommods.reopenedmodularturrets.client.model.SolarAddonModel;
import com.ommods.reopenedmodularturrets.client.model.TurretBaseModel;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
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
        event.registerLayerDefinition(ModModelLayers.TURRET_BASE, TurretBaseModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LEVER_BLOCK, LeverBlockModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.PROJECTILE, ProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TURRET_HEAD.get(), TurretHeadBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SOLAR_ADDON.get(), SolarAddonBlockRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GRENADE_PROJECTILE.get(), GrenadeProjectileRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.LASER_BEAM.get(), LaserBeamRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(lazy(TurretItemRenderer::create), turretItems());
        event.registerItem(lazy(SolarItemRenderer::create), ModItems.SOLAR_ADDON_ITEM.get());
        event.registerItem(lazy(TurretBaseItemRenderer::create),
                ModItems.TURRET_BASE_TIER_1_ITEM.get(),
                ModItems.TURRET_BASE_TIER_2_ITEM.get(),
                ModItems.TURRET_BASE_TIER_3_ITEM.get(),
                ModItems.TURRET_BASE_TIER_4_ITEM.get(),
                ModItems.TURRET_BASE_TIER_5_ITEM.get());
        event.registerItem(lazy(LeverItemRenderer::create), ModItems.LEVER_BLOCK_ITEM.get());
        event.registerItem(lazy(RedstoneReactorItemRenderer::create), ModItems.REDSTONE_REACTOR_ADDON_ITEM.get());
    }

    private static IClientItemExtensions lazy(java.util.function.Supplier<BlockEntityWithoutLevelRenderer> supplier) {
        return new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = supplier.get();
                }
                return renderer;
            }
        };
    }

    private static net.minecraft.world.item.Item[] turretItems() {
        return new net.minecraft.world.item.Item[] {
                ModItems.DISPOSABLE_ITEM_TURRET_ITEM.get(),
                ModItems.POTATO_CANNON_TURRET_ITEM.get(),
                ModItems.GUN_TURRET_ITEM.get(),
                ModItems.GRENADE_TURRET_ITEM.get(),
                ModItems.INCENDIARY_TURRET_ITEM.get(),
                ModItems.ROCKET_TURRET_ITEM.get(),
                ModItems.RELATIVISTIC_TURRET_ITEM.get(),
                ModItems.TELEPORTER_TURRET_ITEM.get(),
                ModItems.LASER_TURRET_ITEM.get(),
                ModItems.RAIL_GUN_TURRET_ITEM.get(),
                ModItems.PLASMA_TURRET_ITEM.get(),
                ModItems.ARC_TURRET_ITEM.get(),
                ModItems.MELEE_TURRET_ITEM.get(),
                ModItems.CROSSBOW_TURRET_ITEM.get()
        };
    }
}
