package com.ommods.reopenedmodularturrets.network.payload;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record SyncArcLightningPayload(BlockPos source, Vec3 from, Vec3 to) implements CustomPacketPayload {
    public static final Type<SyncArcLightningPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "sync_arc_lightning")
    );

    public static final StreamCodec<FriendlyByteBuf, SyncArcLightningPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeBlockPos(payload.source);
                buf.writeDouble(payload.from.x);
                buf.writeDouble(payload.from.y);
                buf.writeDouble(payload.from.z);
                buf.writeDouble(payload.to.x);
                buf.writeDouble(payload.to.y);
                buf.writeDouble(payload.to.z);
            },
            buf -> new SyncArcLightningPayload(
                    buf.readBlockPos(),
                    new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())
            )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
