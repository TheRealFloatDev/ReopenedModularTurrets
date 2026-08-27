package com.ommods.reopenedmodularturrets;

import com.mojang.logging.LogUtils;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.registry.ModBlocks;
import com.ommods.reopenedmodularturrets.registry.ModCreativeTabs;
import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
import com.ommods.reopenedmodularturrets.registry.ModItems;
import com.ommods.reopenedmodularturrets.registry.ModMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(ModConstants.MOD_ID)
public class ReopenedModularTurrets {
    public static final Logger LOGGER = LogUtils.getLogger();

    public ReopenedModularTurrets(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ModNetworking::register);
        modEventBus.addListener(ModCapabilityEvents::registerCapabilities);
        modEventBus.addListener(ReopenedModularTurretsClient::registerScreens);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(ModEvents.class);

        modContainer.registerConfig(Type.SERVER, ModConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Reopened Modular Turrets loaded for NeoForge 1.21.1");
    }
}
