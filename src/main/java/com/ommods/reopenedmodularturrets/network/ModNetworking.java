package com.ommods.reopenedmodularturrets.network;

import com.ommods.reopenedmodularturrets.api.ownership.AccessLevel;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.payload.AdjustLightPayload;
import com.ommods.reopenedmodularturrets.network.payload.AdjustRangePayload;
import com.ommods.reopenedmodularturrets.network.payload.DropBasePayload;
import com.ommods.reopenedmodularturrets.network.payload.DropTurretsPayload;
import com.ommods.reopenedmodularturrets.network.payload.ToggleActivePayload;
import com.ommods.reopenedmodularturrets.network.payload.ToggleMultiTargetPayload;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import com.ommods.reopenedmodularturrets.network.payload.TrustedPlayerPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import com.ommods.reopenedmodularturrets.network.payload.SyncArcLightningPayload;
import com.ommods.reopenedmodularturrets.network.payload.SyncTurretRayPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
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
        registrar.playToServer(
                AdjustRangePayload.TYPE,
                AdjustRangePayload.STREAM_CODEC,
                ModNetworking::handleAdjustRange
        );
        registrar.playToServer(
                ToggleMultiTargetPayload.TYPE,
                ToggleMultiTargetPayload.STREAM_CODEC,
                ModNetworking::handleToggleMultiTarget
        );
        registrar.playToServer(
                ToggleActivePayload.TYPE,
                ToggleActivePayload.STREAM_CODEC,
                ModNetworking::handleToggleActive
        );
        registrar.playToServer(
                DropTurretsPayload.TYPE,
                DropTurretsPayload.STREAM_CODEC,
                ModNetworking::handleDropTurrets
        );
        registrar.playToServer(
                DropBasePayload.TYPE,
                DropBasePayload.STREAM_CODEC,
                ModNetworking::handleDropBase
        );
        registrar.playToServer(
                AdjustLightPayload.TYPE,
                AdjustLightPayload.STREAM_CODEC,
                ModNetworking::handleAdjustLight
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
                        case CHANGE_ACCESS -> base.changeTrustedAccessLevel(
                                payload.playerName(),
                                AccessLevel.fromLevel(payload.accessLevel())
                        );
                    }
                    syncOpenMenu(player, base);
                }
            }
        });
    }

    private static void handleAdjustRange(AdjustRangePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    base.adjustTargetRange(payload.delta());
                    syncOpenMenu(player, base);
                }
            }
        });
    }

    private static void handleToggleMultiTarget(ToggleMultiTargetPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    base.setMultiTargeting(!base.isMultiTargeting());
                    syncOpenMenu(player, base);
                }
            }
        });
    }

    private static void handleToggleActive(ToggleActivePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    base.toggleActive();
                    syncOpenMenu(player, base);
                }
            }
        });
    }

    private static void handleDropTurrets(DropTurretsPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    base.dropTurrets(player);
                }
            }
        });
    }

    private static void handleDropBase(DropBasePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    player.closeContainer();
                    base.dropBase(player);
                }
            }
        });
    }

    private static void handleAdjustLight(AdjustLightPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(payload.pos()) instanceof TurretBaseBlockEntity base
                        && base.canAccess(player)) {
                    if (payload.field() == AdjustLightPayload.LightField.VALUE) {
                        base.setLightValue(payload.value());
                    } else {
                        base.setLightOpacity(payload.value());
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

    public static void sendArcLightning(ServerLevel level, BlockPos source, Vec3 from, Vec3 to) {
        PacketDistributor.sendToPlayersNear(
                level,
                null,
                source.getX() + 0.5,
                source.getY() + 0.5,
                source.getZ() + 0.5,
                128.0,
                new SyncArcLightningPayload(source, from, to)
        );
    }

    public static void sendTurretRay(ServerLevel level, BlockPos source, Vec3 from, Vec3 to, int red, int green, int blue) {
        PacketDistributor.sendToPlayersNear(
                level,
                null,
                source.getX() + 0.5,
                source.getY() + 0.5,
                source.getZ() + 0.5,
                128.0,
                new SyncTurretRayPayload(source, from, to, red, green, blue)
        );
    }
}
