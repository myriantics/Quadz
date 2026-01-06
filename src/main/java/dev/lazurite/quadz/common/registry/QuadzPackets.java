package dev.lazurite.quadz.common.registry;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.networking.c2s.ControllerInputC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.JoystickInputC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.RequestPlayerViewC2SPacket;
import dev.lazurite.quadz.common.networking.c2s.RequestQuadcopterViewC2SPacket;
import dev.lazurite.quadz.common.networking.s2c.JoystickInputS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public abstract class QuadzPackets {
    // c2s
    public static final ResourceLocation JOYSTICK_INPUT_C2S = locateC2S("joystick_input");
    public static final ResourceLocation REQUEST_QUADCOPTER_VIEW_C2S = locateC2S("request_quadcopter_view");
    public static final ResourceLocation REQUEST_PLAYER_VIEW_C2S = locateC2S("request_player_view");
    public static final ResourceLocation CONTROLLER_INPUT_C2S = locateC2S("controller_input");

    // s2c
    public static final ResourceLocation JOYSTICK_INPUT_S2C = locateS2C("joystick_input");

    public static void init() {
        initC2S();
        initS2C();
        QuadzCommon.LOGGER.info("Registered Quadz' Packets!");
    }

    private static void initC2S() {
        registerC2S(JoystickInputC2SPacket.TYPE, JoystickInputC2SPacket.PACKET_CODEC);
        registerC2S(RequestQuadcopterViewC2SPacket.TYPE, RequestQuadcopterViewC2SPacket.PACKET_CODEC);
        registerC2S(RequestPlayerViewC2SPacket.TYPE, RequestPlayerViewC2SPacket.PACKET_CODEC);
        registerC2S(ControllerInputC2SPacket.TYPE, ControllerInputC2SPacket.PACKET_CODEC);
    }

    private static void initS2C() {
        registerS2C(JoystickInputS2CPacket.TYPE, JoystickInputS2CPacket.PACKET_CODEC);
    }

    private static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.playS2C().register(type, codec);
    }

    private static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec){
        PayloadTypeRegistry.playC2S().register(type, codec);
    }

    private static ResourceLocation locateS2C(String name) {
        return QuadzCommon.locate(name + "_s2c");
    }

    private static ResourceLocation locateC2S(String name) {
        return QuadzCommon.locate(name + "_c2s");
    }
}
