package dev.lazurite.quadz.common.networking.c2s;

import dev.lazurite.quadz.common.registry.QuadzPackets;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record JoystickInputC2SPacket(Map<ResourceLocation, Float> values) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, JoystickInputC2SPacket> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    Object2ObjectOpenHashMap::new,
                    ResourceLocation.STREAM_CODEC,
                    ByteBufCodecs.FLOAT
            ),
            JoystickInputC2SPacket::values,
            JoystickInputC2SPacket::new
    );

    public static final Type<JoystickInputC2SPacket> TYPE = new Type<>(QuadzPackets.JOYSTICK_INPUT_C2S);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
