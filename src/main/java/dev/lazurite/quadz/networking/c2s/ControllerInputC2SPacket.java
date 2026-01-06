package dev.lazurite.quadz.networking.c2s;

import dev.lazurite.quadz.control.Joystick;
import dev.lazurite.quadz.registry.QuadzPackets;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ControllerInputC2SPacket(float leftX, float leftY, float rightX, float rightY) implements CustomPacketPayload {
    public ControllerInputC2SPacket(Joystick left, Joystick right) {
        this(left.getX(), left.getY(), right.getX(), right.getY());
    }

    private static final StreamCodec<ByteBuf, Float> BOUNDED_FLOAT_CODEC = ByteBufCodecs.FLOAT.map(
            (aFloat -> Math.clamp(aFloat, -1.0f, 1.0f)),
            (aFloat -> Math.clamp(aFloat, -1.0f, 1.0f))
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ControllerInputC2SPacket> PACKET_CODEC = StreamCodec.composite(
            BOUNDED_FLOAT_CODEC, ControllerInputC2SPacket::leftX,
            BOUNDED_FLOAT_CODEC, ControllerInputC2SPacket::leftY,
            BOUNDED_FLOAT_CODEC, ControllerInputC2SPacket::rightX,
            BOUNDED_FLOAT_CODEC, ControllerInputC2SPacket::rightY,
            ControllerInputC2SPacket::new
    );

    public static final Type<ControllerInputC2SPacket> TYPE = new Type<>(QuadzPackets.CONTROLLER_INPUT_C2S);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
