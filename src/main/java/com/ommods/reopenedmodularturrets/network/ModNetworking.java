package com.ommods.reopenedmodularturrets.network;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworking {
    private ModNetworking() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                ToggleTargetFilterPayload.TYPE,
                ToggleTargetFilterPayload.STREAM_CODEC,
                ModNetworking::handleToggleTarget
        );
    }

    private static void handleToggleTarget(ToggleTargetFilterPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    base.toggleFilter(payload.filter());
                }
            }
        });
    }

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
