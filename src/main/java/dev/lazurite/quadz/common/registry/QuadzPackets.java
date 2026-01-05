package dev.lazurite.quadz.common.registry;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.client.networking.QuadzClientPlayNetworkHandler;
import dev.lazurite.quadz.common.networking.QuadzServerPlayNetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public abstract class QuadzPackets {
    // c2s
    public static final ResourceLocation JOYSTICK_INPUT_C2S = locateC2S("joystick_input");
    // s2c
    public static final ResourceLocation JOYSTICK_INPUT_S2C = locateS2C("joystick_input");
    public static final ResourceLocation REQUEST_QUADCOPTER_VIEW_C2S = locateC2S("request_quadcopter_view");
    public static final ResourceLocation REQUEST_PLAYER_VIEW_C2S = locateC2S("request_player_view");

    public static void init() {
        QuadzCommon.LOGGER.info("Registered Quadz' Packets!");
        initC2S();
    }

    public static void initC2S() {
        registerC2S(JOYSTICK_INPUT_C2S, QuadzServerPlayNetworkHandler::onJoystickInput);
        registerC2S(REQUEST_QUADCOPTER_VIEW_C2S, QuadzServerPlayNetworkHandler::onQuadcopterViewRequested);
        registerC2S(REQUEST_PLAYER_VIEW_C2S, QuadzServerPlayNetworkHandler::onPlayerViewRequestReceived);
    }

    private static void initS2C() {
        registerS2C(JOYSTICK_INPUT_S2C, QuadzClientPlayNetworkHandler::onJoystickInput);
    }

    private static void registerS2C(ResourceLocation id, ClientPlayNetworking.PlayChannelHandler handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, handler);
    }

    private static void registerC2S(ResourceLocation id, ServerPlayNetworking.PlayChannelHandler handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, handler);
    }

    private static ResourceLocation locateS2C(String name) {
        return QuadzCommon.locate(name + "_s2c");
    }

    private static ResourceLocation locateC2S(String name) {
        return QuadzCommon.locate(name + "_c2s");
    }
}
