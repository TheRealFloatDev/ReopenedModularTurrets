package com.ommods.reopenedmodularturrets.network.payload;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.core.targeting.TargetFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ToggleTargetFilterPayload(BlockPos pos, TargetFilter filter) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ToggleTargetFilterPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "toggle_target"));

    public static final StreamCodec<FriendlyByteBuf, ToggleTargetFilterPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ToggleTargetFilterPayload::pos,
            ByteBufCodecs.VAR_INT,
            payload -> payload.filter().ordinal(),
            (pos, ordinal) -> new ToggleTargetFilterPayload(pos, TargetFilter.fromIndex(ordinal))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
