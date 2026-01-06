package dev.lazurite.quadz.networking.s2c;

import dev.lazurite.quadz.registry.QuadzPackets;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record JoystickInputS2CPacket(int playerId, Map<ResourceLocation, Float> values) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, JoystickInputS2CPacket> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, JoystickInputS2CPacket::playerId,
            ByteBufCodecs.map(
                    Object2ObjectOpenHashMap::new,
                    ResourceLocation.STREAM_CODEC,
                    ByteBufCodecs.FLOAT
            ),
            JoystickInputS2CPacket::values,
            JoystickInputS2CPacket::new
    );

    public static final Type<JoystickInputS2CPacket> TYPE = new Type<>(QuadzPackets.JOYSTICK_INPUT_S2C);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
