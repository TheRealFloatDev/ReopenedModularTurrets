package com.ommods.reopenedmodularturrets;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.compat.computercraft.TurretBasePeripheral;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import dan200.computercraft.api.peripheral.PeripheralCapability;
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
        event.registerBlockEntity(
                PeripheralCapability.get(),
                ModBlockEntities.TURRET_BASE.get(),
                (be, side) -> be.isComputerAccessible() ? new TurretBasePeripheral(be) : null
        );
    }
}
