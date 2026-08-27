package com.ommods.reopenedmodularturrets.network.payload;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AdjustLightPayload(BlockPos pos, LightField field, int value) implements CustomPacketPayload {
    public enum LightField {
        VALUE,
        OPACITY
    }

    public static final Type<AdjustLightPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "adjust_light"));

    public static final StreamCodec<FriendlyByteBuf, AdjustLightPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            AdjustLightPayload::pos,
            ByteBufCodecs.VAR_INT,
            payload -> payload.field().ordinal(),
            ByteBufCodecs.VAR_INT,
            AdjustLightPayload::value,
            (pos, fieldOrdinal, value) -> new AdjustLightPayload(pos, LightField.values()[fieldOrdinal], value)
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
