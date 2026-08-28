package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.client.render.ClientTurretEffects;
import com.ommods.reopenedmodularturrets.network.payload.SyncArcLightningPayload;
import com.ommods.reopenedmodularturrets.network.payload.SyncTurretRayPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ClientNetworking {
    private ClientNetworking() {}

    public static void registerClient(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncArcLightningPayload.TYPE,
                SyncArcLightningPayload.STREAM_CODEC,
                ClientNetworking::handleArcLightning
        );
        registrar.playToClient(
                SyncTurretRayPayload.TYPE,
                SyncTurretRayPayload.STREAM_CODEC,
                ClientNetworking::handleTurretRay
        );
    }

    private static void handleArcLightning(SyncArcLightningPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientTurretEffects.spawnLightning(payload.from(), payload.to()));
    }

    private static void handleTurretRay(SyncTurretRayPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientTurretEffects.spawnRay(payload.from(), payload.to(), payload.red(), payload.green(), payload.blue()));
    }
}
