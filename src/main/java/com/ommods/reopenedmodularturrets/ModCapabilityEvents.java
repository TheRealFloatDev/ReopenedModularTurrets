package com.ommods.reopenedmodularturrets;

import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ModCapabilityEvents {
    private ModCapabilityEvents() {}

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.TURRET_BASE.get(),
                (be, side) -> be.getEnergyStorage()
        );
    }
}
