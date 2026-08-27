package com.ommods.reopenedmodularturrets.network.payload;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AdjustRangePayload(BlockPos pos, int delta) implements CustomPacketPayload {
    public static final Type<AdjustRangePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "adjust_range"));

    public static final StreamCodec<FriendlyByteBuf, AdjustRangePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            AdjustRangePayload::pos,
            ByteBufCodecs.INT,
            AdjustRangePayload::delta,
            AdjustRangePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
