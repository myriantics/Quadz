package dev.lazurite.quadz.common.networking.c2s;

import dev.lazurite.quadz.common.registry.QuadzPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RequestPlayerViewC2SPacket() implements CustomPacketPayload {
    public static final RequestPlayerViewC2SPacket INSTANCE = new RequestPlayerViewC2SPacket();

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestPlayerViewC2SPacket> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final Type<RequestPlayerViewC2SPacket> TYPE = new Type<>(QuadzPackets.REQUEST_PLAYER_VIEW_C2S);
}
