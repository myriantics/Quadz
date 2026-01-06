package dev.lazurite.quadz.networking.c2s;

import dev.lazurite.quadz.registry.QuadzPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RequestQuadcopterViewC2SPacket(int clickType) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestQuadcopterViewC2SPacket> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RequestQuadcopterViewC2SPacket::clickType,
            RequestQuadcopterViewC2SPacket::new
    );

    public static final Type<RequestQuadcopterViewC2SPacket> TYPE = new Type<>(QuadzPackets.REQUEST_QUADCOPTER_VIEW_C2S);

    public static final RequestQuadcopterViewC2SPacket LEFT_CLICK = new RequestQuadcopterViewC2SPacket(-1);
    public static final RequestQuadcopterViewC2SPacket RIGHT_CLICK = new RequestQuadcopterViewC2SPacket(1);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
