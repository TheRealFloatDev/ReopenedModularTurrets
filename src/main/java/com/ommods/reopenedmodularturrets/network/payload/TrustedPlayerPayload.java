package com.ommods.reopenedmodularturrets.network.payload;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TrustedPlayerPayload(Action action, BlockPos pos, String playerName) implements CustomPacketPayload {
    public enum Action {
        ADD,
        REMOVE
    }

    public static final CustomPacketPayload.Type<TrustedPlayerPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "trusted_player"));

    public static final StreamCodec<FriendlyByteBuf, TrustedPlayerPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            payload -> payload.action().ordinal(),
            BlockPos.STREAM_CODEC,
            TrustedPlayerPayload::pos,
            ByteBufCodecs.STRING_UTF8,
            TrustedPlayerPayload::playerName,
            (ordinal, pos, name) -> new TrustedPlayerPayload(Action.values()[ordinal], pos, name)
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
