package com.ommods.reopenedmodularturrets.network.payload;

import com.ommods.reopenedmodularturrets.ModConstants;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleMultiTargetPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ToggleMultiTargetPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "toggle_multi_target"));

    public static final StreamCodec<ByteBuf, ToggleMultiTargetPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ToggleMultiTargetPayload::pos,
            ToggleMultiTargetPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
