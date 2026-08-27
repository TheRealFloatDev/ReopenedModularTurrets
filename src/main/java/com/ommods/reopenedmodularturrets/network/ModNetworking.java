package com.ommods.reopenedmodularturrets.network;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import com.ommods.reopenedmodularturrets.network.payload.TrustedPlayerPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
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
        registrar.playToServer(
                TrustedPlayerPayload.TYPE,
                TrustedPlayerPayload.STREAM_CODEC,
                ModNetworking::handleTrustedPlayer
        );
    }

    private static void handleToggleTarget(ToggleTargetFilterPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    base.toggleFilter(payload.filter());
                    syncOpenMenu(player, base);
                }
            }
        });
    }

    private static void handleTrustedPlayer(TrustedPlayerPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    switch (payload.action()) {
                        case ADD -> base.addTrustedPlayer(payload.playerName());
                        case REMOVE -> base.removeTrustedPlayer(payload.playerName());
                    }
                    syncOpenMenu(player, base);
                }
            }
        });
    }

    private static void syncOpenMenu(ServerPlayer player, TurretBaseBlockEntity base) {
        if (player.containerMenu instanceof TurretBaseMenu menu && menu.getBase() == base) {
            menu.refreshData();
            menu.broadcastChanges();
        }
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }
}
