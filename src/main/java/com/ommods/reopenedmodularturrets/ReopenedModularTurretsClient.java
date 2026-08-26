package com.ommods.reopenedmodularturrets;

import com.ommods.reopenedmodularturrets.client.TurretBaseScreen;
import com.ommods.reopenedmodularturrets.registry.ModMenus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class ReopenedModularTurretsClient {
    private ReopenedModularTurretsClient() {}

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.TURRET_BASE.get(), TurretBaseScreen::new);
    }
}
